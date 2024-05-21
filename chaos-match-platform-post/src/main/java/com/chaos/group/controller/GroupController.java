package com.chaos.group.controller;

import com.chaos.annotation.SystemLog;
import com.chaos.group.domain.dto.CreateGroupDto;
import com.chaos.group.domain.dto.ListGroupDto;
import com.chaos.group.domain.dto.ModifyGroupDetailDto;
import com.chaos.group.service.GroupService;
import com.chaos.response.ResponseResult;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @description: 社群Controller
 * @author: xsinxcos
 * @create: 2024-05-09 21:10
 **/

/**
 * 社群模块（用户端）
 */
@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    /**
     * 创建社群
     *
     * @param dto
     * @return
     */
    @PostMapping("/create")
    @SystemLog(BusinessName = "createGroup")
    public ResponseResult<?> createGroup(@RequestBody @Valid CreateGroupDto dto) {
        return groupService.createGroup(dto);
    }

    /**
     * 获取指定社群相关信息
     *
     * @param groupId
     * @return
     */
    @GetMapping("/detail")
    @SystemLog(BusinessName = "groupDetail")
    public ResponseResult<?> groupDetail(@NonNull Long groupId) {
        return groupService.groupDetail(groupId);
    }


    /**
     * 修改指定社群相关信息
     *
     * @param dto
     * @return
     */
    @PutMapping("/detail/modify")
    @SystemLog(BusinessName = "modifyGroupDetail")
    public ResponseResult<?> modifyGroupDetail(@RequestBody @Valid ModifyGroupDetailDto dto) {
        return groupService.modifyGroupDetail(dto);
    }

    /**
     * 罗列社群
     *
     * @param dto
     * @return
     */
    @PostMapping("/list")
    @SystemLog(BusinessName = "listGroup")
    public ResponseResult<?> listGroup(@RequestBody @Valid ListGroupDto dto) {
        return groupService.listGroup(dto);
    }


    /**
     * 获取我的社群
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/getme")
    @SystemLog(BusinessName = "getMyGroup")
    public ResponseResult<?> getMyGroup(@NonNull Integer pageNum ,@NonNull Integer pageSize){
        return groupService.getMyGroup(pageNum ,pageSize);
    }

    /**
     * 获取排名
     * @param groupId
     * @return
     */
    @GetMapping("/team/rank")
    @SystemLog(BusinessName = "getTeamRankInGroup")
    public ResponseResult<?> getTeamRankInGroup(@NonNull Long groupId){
        return groupService.getTeamRankInGroup(groupId);
    }


    /**
     * 退出社群
     * @param groupId
     * @return
     */
    @DeleteMapping("/quit")
    @SystemLog(BusinessName = "quitGroup")
    public ResponseResult<?> quitGroup(@NonNull Long groupId){
        return groupService.quitGroup(groupId);
    }

    /**
     * 获取社群中的队伍
     * @param groupId
     * @return
     */
    @GetMapping("/team/list")
    @SystemLog(BusinessName = "listTeamInGroup")
    public ResponseResult<?> listTeamInGroup(@NonNull Long groupId){
        return groupService.listTeamInGroup(groupId);
    }
}
