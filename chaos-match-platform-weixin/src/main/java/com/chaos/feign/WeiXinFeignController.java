package com.chaos.feign;

import com.chaos.response.ResponseResult;
import com.chaos.service.WeiXinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WeiXinFeignController implements WeiXinFeignClient {
    private final WeiXinService weiXinService;

    /**
     * 从微信端获取登录用户资料
     *
     * @param code
     * @return ResponseResult<WxLoginUserDetailBo>
     */
    @Override
    public ResponseResult wxLoginUserDetail(String code) {
        return weiXinService.wxLoginUserDetail(code);
    }
}
