package com.chaos.group.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaos.group.domain.entity.GroupUser;

import java.util.List;


/**
 * 社群与用户关系表(GroupUser)表数据库访问层
 *
 * @author chaos
 * @since 2024-05-09 21:23:43
 */
public interface GroupUserMapper extends BaseMapper<GroupUser> {
    void saveIgnoreBatchGroupUser(List<GroupUser> groupUsers);
}
