package com.chaos.controller.app;

import cn.hutool.crypto.asymmetric.RSA;
import com.chaos.annotation.SystemLog;
import com.chaos.model.dto.UserInfoDto;
import com.chaos.model.dto.app.UserRegisterDto;
import com.chaos.response.ResponseResult;
import com.chaos.service.AuthUserService;
import com.chaos.util.RSAUtils;
import com.chaos.util.RedisCache;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final AuthUserService authUserService;

    private final RedisCache redisCache;
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

    /**
     * 注册
     * @param userRegisterDto
     * @return
     */
    @PostMapping("/register")
    @SystemLog(BusinessName = "register")
    public ResponseResult register(@RequestBody @Valid UserRegisterDto userRegisterDto) {
        //数据预处理解密
        String publicKey = userRegisterDto.getPublicKey();
        String privateKey = redisCache.getCacheObject(publicKey);

        //解密
        RSA rsa = new RSA(privateKey ,publicKey);
        String decryptEmail = RSAUtils.getDecryptString(userRegisterDto.getEmail(), rsa);
        String decryptPassword = RSAUtils.getDecryptString(userRegisterDto.getPassword(), rsa);
        String decryptUsername = RSAUtils.getDecryptString(userRegisterDto.getUserName(), rsa);

        userRegisterDto.setEmail(decryptEmail);
        userRegisterDto.setPassword(decryptPassword);
        userRegisterDto.setUserName(decryptUsername);

        return authUserService.register(userRegisterDto);
    }
}
