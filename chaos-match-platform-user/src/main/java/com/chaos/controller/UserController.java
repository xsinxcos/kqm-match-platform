package com.chaos.controller;

import com.chaos.response.ResponseResult;
import com.chaos.service.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final AuthUserService authUserService;

    /**
     * 获取用户个人信息
     * @return UserInfoVo
     */
    @GetMapping("/userInfo")
    public ResponseResult getUserInfo(){
        return authUserService.getUserInfo();
    }
}
