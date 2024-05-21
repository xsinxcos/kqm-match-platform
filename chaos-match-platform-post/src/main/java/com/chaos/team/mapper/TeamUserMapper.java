package com.chaos.team.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaos.group.domain.entity.GroupUser;
import com.chaos.team.domain.entity.TeamUser;

import java.util.List;


/**
 * (TeamUser)表数据库访问层
 *
 * @author chaos
 * @since 2024-05-14 23:38:01
 */
public interface TeamUserMapper extends BaseMapper<TeamUser> {
    void saveIgnoreBatchTeamUser(List<TeamUser> teamUsers);
}
