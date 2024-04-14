package com.chaos.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaOpenApiService;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.util.WxMaConfigHolder;
import com.alibaba.nacos.api.utils.StringUtils;
import com.chaos.bo.WxLoginUserDetailBo;
import com.chaos.constant.AppHttpCodeEnum;
import com.chaos.constant.DefaultWxConfig;
import com.chaos.response.ResponseResult;
import com.chaos.service.WeiXinService;
import com.chaos.util.BeanCopyUtils;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class WeiXinServiceImpl implements WeiXinService {
    private final WxMaService wxMaService;


    @Override
    public ResponseResult wxLoginUserDetail(String code) {

        String appid = DefaultWxConfig.WX_APPID;
        if (StringUtils.isBlank(code)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.EMPTY_JSCODE);
        }

        if (!wxMaService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }

        try {
            WxMaJscode2SessionResult sessionInfo = wxMaService.getUserService().getSessionInfo(code);
            WxLoginUserDetailBo bo = BeanCopyUtils.copyBean(sessionInfo, WxLoginUserDetailBo.class);

            return ResponseResult.okResult(bo);
        } catch (WxErrorException ignored) {
        } finally {
            WxMaConfigHolder.remove();//清理ThreadLocal
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.WEIXIN_LOGIN_FAIL);
    }
}
