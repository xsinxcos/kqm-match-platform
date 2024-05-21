package com.chaos.team.controller;

import com.chaos.annotation.SystemLog;
import com.chaos.response.ResponseResult;
import com.chaos.team.domain.dto.ModifyTeamDetailDto;
import com.chaos.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author : wzq
 * @since : 2024-05-16 00:37
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/team")
public class TeamController {
    private final TeamService teamService;


    /**
     * 获取社群队伍信息
     * @param pageNum
     * @param pageSize
     * @param groupId
     * @return
     */
    @GetMapping("/listInGroup")
    @SystemLog(BusinessName = "listTeamByGroupId")
    public ResponseResult listTeamByGroupId(@NotNull Integer pageNum,@NotNull Integer pageSize ,@NotNull Long groupId){
        return teamService.listTeamByGroupId(pageNum ,pageSize ,groupId);
    }


    /**
     * 获取我加入的队伍
     * @return
     */
    @GetMapping("/getme")
    @SystemLog(BusinessName = "getMyTeams")
    public ResponseResult<?> getMyTeams(@NotNull Integer pageNum ,@NotNull Integer pageSize){
        return teamService.getMyTeams(pageNum ,pageSize);
    }

    /**
     * 修改队伍信息
     * @param dto
     * @return
     */
    @PutMapping("/detail/modify")
    @SystemLog(BusinessName = "modifyTeamDetail")
    public ResponseResult<?> modifyTeamDetail(@RequestBody @Valid ModifyTeamDetailDto dto){
        return teamService.modifyTeamDetail(dto);
    }

    /**
     * 获取队伍的成员信息
     * @param teamId
     * @return
     */
    @GetMapping("/member/list")
    @SystemLog(BusinessName = "teamMembersList")
    public ResponseResult<?> teamMembersList(@NotNull Long teamId){
        return teamService.teamMembersList(teamId);
    }
}
