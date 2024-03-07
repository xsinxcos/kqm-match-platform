package com.chaos.controller.admin;

import com.chaos.annotation.AuthAdminCheck;
import com.chaos.response.ResponseResult;
import com.chaos.service.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 管理端用户
 * @author: xsinxcos
 * @create: 2024-03-07 17:11
 **/
@RestController
@RequestMapping("/manage")
@RequiredArgsConstructor
public class AdminUserController {
    private final AuthUserService authUserService;

    /**
     * 管理端重置新密码
     * @param uid
     * @return
     */
    @AuthAdminCheck
    @PostMapping("/resetPassword")
    public ResponseResult resetPassword(String uid){
        return authUserService.resetPasswordById(uid);
    }
}
