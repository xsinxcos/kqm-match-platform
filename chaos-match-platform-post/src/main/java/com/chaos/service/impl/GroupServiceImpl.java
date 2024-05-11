package com.chaos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.domain.bo.GroupOwnerBo;
import com.chaos.domain.dto.app.CreateGroupDto;
import com.chaos.domain.dto.app.ModifyGroupDetailDto;
import com.chaos.domain.entity.Group;
import com.chaos.domain.entity.GroupUser;
import com.chaos.domain.vo.app.GroupDetailVo;
import com.chaos.feign.UserFeignClient;
import com.chaos.feign.bo.AuthUserBo;
import com.chaos.mapper.GroupMapper;
import com.chaos.response.ResponseResult;
import com.chaos.service.GroupService;
import com.chaos.service.GroupUserService;
import com.chaos.util.BeanCopyUtils;
import com.chaos.util.SecurityUtils;
import com.chaos.util.SnowFlakeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

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

    private final static int GROUP_USER_SUPER_ADMIN = 2;

    private final UserFeignClient userFeignClient;

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
        AuthUserBo authUserBo = userFeignClient.getUserById(userId).getData();

        GroupDetailVo vo = BeanCopyUtils.copyBean(group, GroupDetailVo.class);
        vo.setOwner(GroupOwnerBo.builder()
                .uid(authUserBo.getId())
                .sex(authUserBo.getSex())
                .avatar(authUserBo.getAvatar())
                .selfLabel(authUserBo.getSelfLabel())
                .username(authUserBo.getUserName())
                .build());

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

        //更新社群基本信息
        updateById(group);
        return ResponseResult.okResult();
    }
}

