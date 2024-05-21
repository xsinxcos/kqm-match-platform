package com.chaos.mail.controller;

import com.chaos.annotation.SystemLog;
import com.chaos.common.util.GroupMemberUtils;
import com.chaos.mail.domain.dto.ApplyGroupDto;
import com.chaos.mail.domain.dto.HandleGroupApplicationDto;
import com.chaos.mail.service.MailService;
import com.chaos.response.ResponseResult;
import com.chaos.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author : wzq
 * @since : 2024-05-18 22:37
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class MailController {

    private final MailService mailService;

    private final GroupMemberUtils groupMemberUtils;

    /**
     * 获取系统邮件列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/sys/getme")
    @SystemLog(BusinessName = "getMyMail")
    public ResponseResult<?> getMeSysMail(@NotNull Integer pageNum ,@NotNull Integer pageSize){
        return mailService.getMyMail(pageNum ,pageSize);
    }

    /**
     * 申请进入社群
     * @param dto
     * @return
     */
    @PostMapping("/applyGroup")
    @SystemLog(BusinessName = "applyGroup")
    public ResponseResult<?> applyGroup(@RequestBody @Valid ApplyGroupDto dto){
        Long userId = SecurityUtils.getUserId();
        boolean is = groupMemberUtils.isGroupMember(userId, dto.getGroupId());
        if(is) throw new RuntimeException("已经是社群成员，无需再次申请");
        return mailService.applyGroup(dto);
    }

    /**
     * 处理进群申请
     * @return
     */
    @PostMapping("/applyGroup/handle")
    @SystemLog(BusinessName = "handleGroupApplication")
    public ResponseResult<?> handleGroupApplication(@RequestBody @Valid HandleGroupApplicationDto dto){
        Long userId = SecurityUtils.getUserId();
        boolean is = groupMemberUtils.isGroupAdmin(userId, dto.getGroupId());
        if(!is) throw new RuntimeException("无权限");
        return mailService.handleGroupApplication(dto);
    }


}
