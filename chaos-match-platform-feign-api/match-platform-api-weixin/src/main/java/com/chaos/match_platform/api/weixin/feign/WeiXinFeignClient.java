package com.chaos.match_platform.api.weixin.feign;

import com.chaos.match_platform.api.weixin.bo.WxLoginUserDetailBo;
import com.chaos.response.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "WeiXinService")
public interface WeiXinFeignClient {
    @GetMapping("/feign/getWxLoginUserDetail")
    ResponseResult<WxLoginUserDetailBo> wxLoginUserDetail(@RequestParam("code") String code);
}
