package com.chaos.controller;

import com.chaos.bo.WxLoginUserDetailBo;
import com.chaos.constant.AppHttpCodeEnum;
import com.chaos.feign.WeiXinFeignClient;
import com.chaos.response.ResponseResult;
import com.chaos.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class LoginController {

    private final WeiXinFeignClient weiXinFeignClient;

    private final AuthService authService;

    /**
     * 用户微信登录
     * @param code 微信登录code
     * @return token
     */
    @GetMapping("/wxLogin")
    public ResponseResult wxlogin(String code){
        WxLoginUserDetailBo detailBo = weiXinFeignClient.wxLoginUserDetail(code).getData();
        if(Objects.isNull(detailBo.getOpenid())){
            ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR);
        }
        return authService.wxlogin(detailBo.getOpenid());
    }

    /**
     * 退出登录
     * @return void
     */
    @PostMapping("/logout")
    public ResponseResult logout(){
        return authService.logout();
    }
}
