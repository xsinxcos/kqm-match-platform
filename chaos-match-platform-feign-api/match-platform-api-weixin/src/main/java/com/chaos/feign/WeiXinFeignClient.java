package com.chaos.feign;

import com.chaos.bo.WxLoginUserDetailBo;
import com.chaos.response.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 微信模块远程调用
 */
@FeignClient(value = "WeiXinService")
public interface WeiXinFeignClient {
    /**
     * 获取微信登录凭证
     * @param code
     * @return
     */
    @GetMapping("/feign/getWxLoginUserDetail")
    ResponseResult<WxLoginUserDetailBo> wxLoginUserDetail(@RequestParam("code") String code);
}
