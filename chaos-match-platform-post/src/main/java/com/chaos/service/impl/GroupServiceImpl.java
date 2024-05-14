package com.chaos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.config.vo.PageVo;
import com.chaos.domain.bo.CategoryBo;
import com.chaos.domain.bo.GroupOwnerBo;
import com.chaos.domain.dto.app.CreateGroupDto;
import com.chaos.domain.dto.app.ListGroupDto;
import com.chaos.domain.dto.app.ModifyGroupDetailDto;
import com.chaos.domain.entity.Category;
import com.chaos.domain.entity.Group;
import com.chaos.domain.entity.GroupUser;
import com.chaos.domain.entity.PostUser;
import com.chaos.domain.vo.app.GroupDetailVo;
import com.chaos.domain.vo.app.GroupListVo;
import com.chaos.feign.UserFeignClient;
import com.chaos.feign.bo.AddPostGroupRelationBo;
import com.chaos.feign.bo.AuthUserBo;
import com.chaos.mapper.GroupMapper;
import com.chaos.mapper.GroupUserMapper;
import com.chaos.response.ResponseResult;
import com.chaos.service.CategoryService;
import com.chaos.service.GroupService;
import com.chaos.service.GroupUserService;
import com.chaos.service.PostUserService;
import com.chaos.util.BeanCopyUtils;
import com.chaos.util.SecurityUtils;
import com.chaos.util.SnowFlakeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * 小社区（社群）(Group)表服务实现类
 *
 * @author chaos
 * @since 2024-05-09 21:09:08
 */
@Service("groupService")
@RequiredArgsConstructor
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements GroupService {

    private final GroupUserService groupUserService;

    private final PostUserService postUserService;

    private final GroupUserMapper groupUserMapper;

    private final CategoryService categoryService;
    private final static int GROUP_USER_SUPER_ADMIN = 2;

    private final static int GROUP_USER_COMMON_USER = 0;

    private final UserFeignClient userFeignClient;

    private static final int USER_POST_MATCH_STATUS = 0;

    @Override
    public ResponseResult createGroup(CreateGroupDto dto) {
        //获取分布式ID
        Long flakeId = SnowFlakeUtil.getDefaultSnowFlakeId();
        Group newGroup = Group.builder()
                .id(flakeId)
                .label(dto.getLabel())
                .name(dto.getName())
                .icon(dto.getIcon())
                .categoryId(dto.getCategoryId())
                .build();
        save(newGroup);
        //保存用户与社群的关系
        groupUserService.save(
                GroupUser.builder()
                        .groupId(newGroup.getId())
                        .userId(SecurityUtils.getUserId())
                        .type(GROUP_USER_SUPER_ADMIN)
                        .build()
        );
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult groupDetail(Long groupId) {
        Group group = getById(groupId);
        //查询现群主
        GroupUser groupUser = groupUserService.getBaseMapper().selectOne((
                new LambdaQueryWrapper<GroupUser>()
                        .eq(GroupUser::getGroupId, groupId)
                        .eq(GroupUser::getType, GROUP_USER_SUPER_ADMIN)));

        Long userId = groupUser.getUserId();
        //查询现群主详细信息
        CompletableFuture<AuthUserBo> authUserBoCompletableFuture = CompletableFuture.supplyAsync(() -> {
            return userFeignClient.getUserById(userId).getData();
        });
        //查询社群的类别信息
        CompletableFuture<Category> groupCategoryFuture = CompletableFuture.supplyAsync(() -> {
            return categoryService.getById(group.getCategoryId());
        });
        Category category = null;
        AuthUserBo authUserBo = null;
        try {
            category = groupCategoryFuture.get();
            authUserBo = authUserBoCompletableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        //生成VO
        GroupDetailVo vo = BeanCopyUtils.copyBean(group, GroupDetailVo.class);
        vo.setOwner(GroupOwnerBo.builder()
                .uid(authUserBo.getId())
                .sex(authUserBo.getSex())
                .avatar(authUserBo.getAvatar())
                .selfLabel(authUserBo.getSelfLabel())
                .username(authUserBo.getUserName())
                .build());
        vo.setCategory(new CategoryBo(category.getId(), category.getName()));
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult modifyGroupDetail(ModifyGroupDetailDto dto) {
        //检验是否为社群超级管理员
        GroupUser groupUser = groupUserService.getBaseMapper().selectOne(
                new LambdaQueryWrapper<GroupUser>()
                        .eq(GroupUser::getGroupId, dto.getId()));
        if (Objects.isNull(groupUser) || !groupUser.getUserId().equals(SecurityUtils.getUserId())) {
            throw new RuntimeException("修改失败");
        }

        //进行修改
        Group group = getById(dto.getId());
        group.setName(dto.getName());
        group.setIcon(dto.getIcon());
        group.setLabel(dto.getLabel());
        group.setIntroduction(dto.getIntroduction());
        group.setCategoryId(dto.getCategoryId());

        //更新社群基本信息
        updateById(group);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addPostGroupRelation(AddPostGroupRelationBo addPostGroupRelationBo) {
        //获取帖子对应队伍的成员
        List<PostUser> postUsers = postUserService.getBaseMapper().selectList(
                new LambdaQueryWrapper<PostUser>()
                        .eq(PostUser::getPostId, addPostGroupRelationBo.getPostId())
                        .eq(PostUser::getStatus, USER_POST_MATCH_STATUS)
        );
        //队伍总成员
        List<Long> userIds = postUsers.stream().map(PostUser::getUserId).collect(Collectors.toList());
        Long groupId = addPostGroupRelationBo.getGroupId();
        //将队伍成员与群组进行关联，默认普通用户
        List<GroupUser> groupUserList = new ArrayList<>();
        userIds.forEach(o -> groupUserList.add(
                GroupUser.builder()
                        .groupId(groupId)
                        .userId(o)
                        .type(GROUP_USER_COMMON_USER)
                        .createTime(new Date())
                        .createTime(new Date())
                        .build()
        ));
        //批量保存
        groupUserMapper.saveIgnoreBatchGroupUser(groupUserList);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listGroup(ListGroupDto dto) {
        LambdaQueryWrapper<Group> wrapper = new LambdaQueryWrapper<>();
        //筛选
        wrapper.eq(Objects.nonNull(dto.getCategoryId()), Group::getCategoryId, dto.getCategoryId());
        //分页
        Page<Group> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        page(page, wrapper);
        //封装VO
        List<GroupListVo> vos = BeanCopyUtils.copyBeanList(page.getRecords(), GroupListVo.class);
        //获取成员数
        for (GroupListVo record : vos) {
            Integer count = groupUserMapper.selectCount(
                    new LambdaQueryWrapper<GroupUser>()
                            .eq(GroupUser::getGroupId, record.getId()));
            record.setMembersCount(Long.valueOf(count));
        }
        return ResponseResult.okResult(new PageVo(vos, page.getTotal()));
    }
}

