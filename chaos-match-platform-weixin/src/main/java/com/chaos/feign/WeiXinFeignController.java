package com.chaos.feign;

import com.chaos.match_platform.api.weixin.feign.WeiXinFeignClient;
import com.chaos.response.ResponseResult;
import com.chaos.service.WeiXinService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WeiXinFeignController implements WeiXinFeignClient {
    private final WeiXinService weiXinService;
    @Override
    public ResponseResult wxLoginUserDetail(String code) {
        return weiXinService.wxLoginUserDetail(code);
    }
}
