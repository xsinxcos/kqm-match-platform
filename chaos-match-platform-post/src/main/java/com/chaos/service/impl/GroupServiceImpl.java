package com.chaos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.domain.bo.CategoryBo;
import com.chaos.domain.bo.GroupOwnerBo;
import com.chaos.domain.dto.app.CreateGroupDto;
import com.chaos.domain.dto.app.ModifyGroupDetailDto;
import com.chaos.domain.entity.*;
import com.chaos.domain.vo.app.GroupDetailVo;
import com.chaos.feign.UserFeignClient;
import com.chaos.feign.bo.AddPostGroupRelationBo;
import com.chaos.feign.bo.AuthUserBo;
import com.chaos.mapper.GroupMapper;
import com.chaos.mapper.GroupUserMapper;
import com.chaos.response.ResponseResult;
import com.chaos.service.*;
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

    private final GroupCategoryService groupCategoryService;

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

        //保存社群与类别的关系
        groupCategoryService.save(new GroupCategory(newGroup.getId() ,dto.getCategoryId()));
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
        CompletableFuture<AuthUserBo> authUserBoCompletableFuture = CompletableFuture.supplyAsync(()-> {
                return userFeignClient.getUserById(userId).getData();
        });
        //查询社群的类别信息
        CompletableFuture<GroupCategory> groupCategoryFuture = CompletableFuture.supplyAsync(()-> {
            return groupCategoryService.getOne(new LambdaQueryWrapper<GroupCategory>().eq(GroupCategory::getGroupId ,groupId));
        });
        GroupCategory category = null;
        AuthUserBo authUserBo = null;
        try {
            category = groupCategoryFuture.get();
            authUserBo = authUserBoCompletableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        //获取分类详细信息
        Long categoryId = category.getCategoryId();
        Category byId = categoryService.getById(categoryId);
        //生成VO
        GroupDetailVo vo = BeanCopyUtils.copyBean(group, GroupDetailVo.class);
        vo.setOwner(GroupOwnerBo.builder()
                .uid(authUserBo.getId())
                .sex(authUserBo.getSex())
                .avatar(authUserBo.getAvatar())
                .selfLabel(authUserBo.getSelfLabel())
                .username(authUserBo.getUserName())
                .build());
        vo.setCategory(new CategoryBo(byId.getId() ,byId.getName()));
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

        //删除原有类别与社群关系
        groupCategoryService.getBaseMapper().delete(new LambdaQueryWrapper<GroupCategory>()
                .eq(GroupCategory::getGroupId ,dto.getId()));

        //进行修改
        Group group = getById(dto.getId());
        group.setName(dto.getName());
        group.setIcon(dto.getIcon());
        group.setLabel(dto.getLabel());
        group.setIntroduction(dto.getIntroduction());


        //更新社群基本信息
        //保存社群与类别的关系
        groupCategoryService.save(new GroupCategory(dto.getId() ,dto.getCategoryId()));
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
}

