package com.chaos.common.util;

import com.chaos.group.domain.entity.GroupUser;
import com.chaos.group.service.GroupUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author : wzq
 * @since : 2024-05-18 20:45
 **/
@Component
@RequiredArgsConstructor
public class GroupMemberUtils {
    private final GroupUserService groupUserService;

    public boolean isGroupMember(Long userId ,Long groupId){
        Integer i = groupUserService.lambdaQuery().eq(Objects.nonNull(userId), GroupUser::getUserId, userId)
                .eq(Objects.nonNull(groupId), GroupUser::getGroupId, groupId)
                .count();
        return i > 0;
    }

    public boolean isGroupAdmin(Long userId ,Long groupId){
        Integer i = groupUserService.lambdaQuery().eq(Objects.nonNull(userId), GroupUser::getUserId, userId)
                .eq(Objects.nonNull(groupId), GroupUser::getGroupId, groupId)
                .eq(GroupUser::getType, 2)
                .count();
        return i > 0;
    }
}
