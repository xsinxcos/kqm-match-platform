package com.chaos.controller.app;

import com.chaos.annotation.SystemLog;
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
    @SystemLog(BusinessName = "getUserInfo")
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
    @SystemLog(BusinessName = "updateUserInfo")
    public ResponseResult updateUserInfo(@RequestBody UserInfoDto userInfoDto) {
        return authUserService.updateUserInfo(userInfoDto);
    }

    /**
     * 获取其他用户个人信息
     *
     * @param userId
     * @return
     */

    @GetMapping("/userInfoById")
    @SystemLog(BusinessName = "getUserInfoById")
    public ResponseResult getUserInfoById(@NonNull Long userId) {
        return authUserService.getUserInfoById(userId);
    }

    //todo 注册
    @PostMapping("/register")
    @SystemLog(BusinessName = "register")
    public ResponseResult register() {
        return ResponseResult.okResult();
    }
}
