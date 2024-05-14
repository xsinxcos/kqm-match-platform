package com.chaos.controller.app;

import com.chaos.annotation.SystemLog;
import com.chaos.domain.dto.app.CreateGroupDto;
import com.chaos.domain.dto.app.ListGroupDto;
import com.chaos.domain.dto.app.ModifyGroupDetailDto;
import com.chaos.response.ResponseResult;
import com.chaos.service.GroupService;
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
    public ResponseResult createGroup(@RequestBody @Valid CreateGroupDto dto) {
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
    public ResponseResult groupDetail(@NonNull Long groupId) {
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
    public ResponseResult modifyGroupDetail(@RequestBody @Valid ModifyGroupDetailDto dto) {
        return groupService.modifyGroupDetail(dto);
    }

    /**
     * 罗列社群
     * @param dto
     * @return
     */
    @PostMapping("/list")
    @SystemLog(BusinessName = "listGroup")
    public ResponseResult listGroup(@RequestBody @Valid ListGroupDto dto){
        return groupService.listGroup(dto);
    }
}
