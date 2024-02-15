package com.chaos.controller;

import com.chaos.model.dto.UserInfoDto;
import com.chaos.response.ResponseResult;
import com.chaos.service.AuthUserService;
import lombok.NonNull;
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

    /**
     * 获取其他用户个人信息
     * @param userId
     * @return
     */

    @GetMapping("/userInfoById")
    public ResponseResult getUserInfoById(@NonNull Long userId){
        return authUserService.getUserInfoById(userId);
    }
    //todo 注册
    @PostMapping("/register")
    public ResponseResult register() {
        return ResponseResult.okResult();
    }
}
