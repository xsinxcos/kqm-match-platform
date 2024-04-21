package com.chaos.controller.admin;

import com.chaos.annotation.AuthAdminCheck;
import com.chaos.annotation.SystemLog;
import com.chaos.model.dto.admin.EditAccessRightsDto;
import com.chaos.model.dto.admin.ResetPasswordDto;
import com.chaos.model.dto.admin.UserListDto;
import com.chaos.model.dto.admin.UserStatusChangeDto;
import com.chaos.response.ResponseResult;
import com.chaos.service.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 管理端用户
 * @author: xsinxcos
 * @create: 2024-03-07 17:11
 **/

/**
 * 用户模块（管理端）
 */
@RestController
@RequestMapping("/user/manage")
@RequiredArgsConstructor
public class AdminUserController {
    private final AuthUserService authUserService;

    /**
     * 管理端重置新密码
     *
     * @param dto
     * @return
     */
    @AuthAdminCheck
    @PostMapping("/resetPassword")
    @SystemLog(BusinessName = "resetPassword")
    public ResponseResult resetPassword(@RequestBody ResetPasswordDto dto) {
        return authUserService.resetPasswordById(dto.getUid());
    }

    /**
     * 根据条件罗列用户
     *
     * @param userListDto
     * @return
     */
    @AuthAdminCheck
    @PostMapping("/userList")
    @SystemLog(BusinessName = "userList")
    public ResponseResult userList(@RequestBody UserListDto userListDto) {
        return authUserService.userList(userListDto);
    }

    /**
     * 修改用户状态
     *
     * @param userStatusChangeDto
     * @return
     */
    @AuthAdminCheck
    @PostMapping("/status/change")
    @SystemLog(BusinessName = "changeUserStatus")
    public ResponseResult changeUserStatus(@RequestBody UserStatusChangeDto userStatusChangeDto) {
        return authUserService.ChangeUserStatus(userStatusChangeDto);
    }

    /**
     * 修改管理员权限
     *
     * @param editAccessRightsDto
     * @return
     */

    @AuthAdminCheck
    @PostMapping("/administrator")
    @SystemLog(BusinessName = "editAccessRights")
    public ResponseResult editAccessRights(@RequestBody EditAccessRightsDto editAccessRightsDto) {
        return authUserService.editAccessRights(editAccessRightsDto);
    }
}
