package com.chaos.user.controller.app;

import cn.hutool.crypto.asymmetric.RSA;
import com.chaos.annotation.SystemLog;
import com.chaos.user.domain.dto.UserInfoDto;
import com.chaos.user.domain.dto.app.PasswordForgetDto;
import com.chaos.user.domain.dto.app.VerificationCodeDto;
import com.chaos.user.domain.dto.app.UserRegisterDto;
import com.chaos.response.ResponseResult;
import com.chaos.user.service.AuthUserService;
import com.chaos.util.RSAUtils;
import com.chaos.util.RedisCache;
import com.chaos.utils.VerifyCodeUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * 用户模块（APP端）
 */
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
     *
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
        RSA rsa = new RSA(privateKey, publicKey);
        String decryptEmail = RSAUtils.getDecryptString(userRegisterDto.getEmail(), rsa);
        String decryptPassword = RSAUtils.getDecryptString(userRegisterDto.getPassword(), rsa);
        String decryptUsername = RSAUtils.getDecryptString(userRegisterDto.getUserName(), rsa);

        //对RSA进行清除
        redisCache.deleteObject(publicKey);

        //邮箱验证码校验
        String key = VerifyCodeUtils.CACHE_REGISTER_CODE_PREFIX + decryptEmail;
        String realCode = redisCache.getCacheObject(key);
        if(!userRegisterDto.getCode().equals(realCode)){
            throw new RuntimeException("验证码错误");
        }else {
            redisCache.deleteObject(key);
        }

        userRegisterDto.setEmail(decryptEmail);
        userRegisterDto.setPassword(decryptPassword);
        userRegisterDto.setUserName(decryptUsername);

        return authUserService.register(userRegisterDto);
    }

    /**
     * 忘记密码
     * @param dto
     * @return
     */
    @PostMapping("/forgetPassword")
    @SystemLog(BusinessName = "forgetPassword")
    public ResponseResult forgetPassword(@RequestBody @Valid PasswordForgetDto dto){
        //数据预处理解密
        String publicKey = dto.getPublicKey();
        String privateKey = redisCache.getCacheObject(publicKey);

        //解密
        RSA rsa = new RSA(privateKey, publicKey);
        String decryptEmail = RSAUtils.getDecryptString(dto.getEmail(), rsa);
        String decryptPassword = RSAUtils.getDecryptString(dto.getNewPassword(), rsa);

        //对RSA进行清除
        redisCache.deleteObject(publicKey);

        //邮箱验证码校验
        String key = VerifyCodeUtils.CACHE_PASSWORD_FORGET_CODE_PREFIX + decryptEmail;
        String realCode = redisCache.getCacheObject(key);

        if(!dto.getCode().equals(realCode)){
            throw new RuntimeException("验证码错误");
        }else {
            redisCache.deleteObject(key);
        }

        dto.setEmail(decryptEmail);
        dto.setNewPassword(decryptPassword);

        return authUserService.forgetPassword(dto);
    }


    /**
     * 发送验证码到指定邮箱（注册）
     *
     * @param dto
     * @return
     */
    @PostMapping("/sendRegisterCodeToEmail")
    @SystemLog(BusinessName = "sendRegisterCodeToEmail")
    public ResponseResult sendRegisterCodeToEmail(@RequestBody @Valid VerificationCodeDto dto) {
        Long expire = redisCache.getExpire(VerifyCodeUtils.CACHE_REGISTER_CODE_PREFIX + dto.getEmail());
        //对发送频率进行限制，同一个邮箱60s限制
        long limit = (VerifyCodeUtils.timeOutToCode - VerifyCodeUtils.timeOutToSend);
        if(expire > limit){
            throw new RuntimeException("邮件发送过于频繁");
        }

        return authUserService.sendRegisterCodeToEmail(dto);
    }


    /**
     * 发送验证码到指定邮箱（修改密码）
     * @param dto
     * @return
     */
    @PostMapping("/sendPWResetCodeToEmail")
    @SystemLog(BusinessName = "sendPWResetCodeToEmail")
    public ResponseResult sendPWResetCodeToEmail(@RequestBody @Valid VerificationCodeDto dto) {
        Long expire = redisCache.getExpire(VerifyCodeUtils.CACHE_PASSWORD_FORGET_CODE_PREFIX + dto.getEmail());
        //对发送频率进行限制，同一个邮箱60s限制
        long limit = (VerifyCodeUtils.timeOutToCode - VerifyCodeUtils.timeOutToSend);
        if (expire > limit) {
            throw new RuntimeException("邮件发送过于频繁");
        }

        return authUserService.sendPWResetCodeToEmail(dto);
    }


    /**
     * 检查邮箱是否被注册(注册和修改密码前优先调用确认)
     * @param email 邮箱
     * @return
     */
    @GetMapping("/checkEmailExist")
    @SystemLog(BusinessName = "checkEmailExist")
    public ResponseResult checkEmailExist(@NotBlank String email){
        return authUserService.checkEmailExist(email);
    }

}
