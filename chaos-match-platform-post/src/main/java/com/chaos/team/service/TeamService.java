package com.chaos.team.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaos.feign.bo.AddTeamUserMatchRelationBo;
import com.chaos.response.ResponseResult;
import com.chaos.team.domain.dto.ModifyTeamDetailDto;
import com.chaos.team.domain.entity.Team;


/**
 * (Team)表服务接口
 *
 * @author chaos
 * @since 2024-05-14 23:37:49
 */
public interface TeamService extends IService<Team> {

    ResponseResult addTeamUserMatchRelation(AddTeamUserMatchRelationBo addTeamUserMatchRelationBo);

    ResponseResult listTeamByGroupId(Integer pageNum, Integer pageSize, Long groupId);

    ResponseResult<?> getMyTeams(Integer pageNum, Integer pageSize);

    ResponseResult<?> modifyTeamDetail(ModifyTeamDetailDto dto);

    ResponseResult<?> teamMembersList(Long teamId);
}

