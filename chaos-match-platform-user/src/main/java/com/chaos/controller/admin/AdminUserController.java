package com.chaos.controller.admin;

import com.chaos.annotation.AuthAdminCheck;
import com.chaos.annotation.SystemLog;
import com.chaos.response.ResponseResult;
import com.chaos.service.AuthUserService;
import com.chaos.vo.admin.EditAccessRightsVo;
import com.chaos.vo.admin.ResetPasswordVo;
import com.chaos.vo.admin.UserListVo;
import com.chaos.vo.admin.UserStatusChangeVo;
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
@RestController
@RequestMapping("/user/manage")
@RequiredArgsConstructor
public class AdminUserController {
    private final AuthUserService authUserService;

    /**
     * 管理端重置新密码
     * @param vo
     * @return
     */
    @AuthAdminCheck
    @PostMapping("/resetPassword")
    @SystemLog(BusinessName = "resetPassword")
    public ResponseResult resetPassword(@RequestBody ResetPasswordVo vo){
        return authUserService.resetPasswordById(vo.getUid());
    }

    /**
     * 根据条件罗列用户
     * @param userListVo
     * @return
     */
    @AuthAdminCheck
    @PostMapping("/userList")
    @SystemLog(BusinessName = "userList")
    public ResponseResult userList(@RequestBody UserListVo userListVo){
        return authUserService.userList(userListVo);
    }

    /**
     * 修改用户状态
     * @param userStatusChangeVo
     * @return
     */
    @AuthAdminCheck
    @PostMapping("/status/change")
    @SystemLog(BusinessName = "changeUserStatus")
    public ResponseResult changeUserStatus(@RequestBody UserStatusChangeVo userStatusChangeVo){
        return authUserService.ChangeUserStatus(userStatusChangeVo);
    }

    /**
     * 修改管理员权限
     * @param vo
     * @return
     */

    @AuthAdminCheck
    @PostMapping("/user/manage/administrator")
    @SystemLog(BusinessName = "editAccessRights")
    public ResponseResult editAccessRights(@RequestBody EditAccessRightsVo vo){
        return authUserService.editAccessRights(vo);
    }
}
