package com.chaos.controller;

import com.chaos.model.dto.UserInfoDto;
import com.chaos.response.ResponseResult;
import com.chaos.service.AuthUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final AuthUserService authUserService;

    /**
     * 获取用户个人信息
     *
     * @return UserInfoVo
     */
    @GetMapping("/userInfo")
    public ResponseResult getUserInfo() {
        return authUserService.getUserInfo();
    }

    /**
     * 更新个人信息
     *
     * @param userInfoDto
     * @return
     */
    @PutMapping("/userInfo")
    public ResponseResult updateUserInfo(@RequestBody UserInfoDto userInfoDto) {
        return authUserService.updateUserInfo(userInfoDto);
    }

    @GetMapping("/userInfoById")
    public ResponseResult getUserInfoById(Long userId){
        return authUserService.getUserInfoById(userId);
    }

    @PostMapping("/register")
    public ResponseResult register() {
        return ResponseResult.okResult();
    }
}
