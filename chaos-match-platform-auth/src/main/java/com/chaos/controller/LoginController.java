package com.chaos.controller;

import com.chaos.constant.AppHttpCodeEnum;
import com.chaos.match_platform.api.weixin.bo.WxLoginUserDetailBo;
import com.chaos.match_platform.api.weixin.feign.WeiXinFeignClient;
import com.chaos.response.ResponseResult;
import com.chaos.service.AuthUserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class LoginController {

    private final WeiXinFeignClient weiXinFeignClient;

    private final AuthUserService authUserService;
    @GetMapping("/wxLogin")
    public ResponseResult wxlogin(String code){
        WxLoginUserDetailBo detailBo = weiXinFeignClient.wxLoginUserDetail(code).getData();
        if(Objects.isNull(detailBo.getOpenid())){
            ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR);
        }
        return authUserService.wxlogin(detailBo.getOpenid());
    }
}
