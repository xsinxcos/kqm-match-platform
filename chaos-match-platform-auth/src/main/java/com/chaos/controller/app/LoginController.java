package com.chaos.controller.app;

import com.chaos.annotation.SystemLog;
import com.chaos.bo.WxLoginUserDetailBo;
import com.chaos.constant.AppHttpCodeEnum;
import com.chaos.entity.dto.PasswordLoginDto;
import com.chaos.exception.SystemException;
import com.chaos.feign.WeiXinFeignClient;
import com.chaos.response.ResponseResult;
import com.chaos.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class LoginController {

    private final WeiXinFeignClient weiXinFeignClient;

    private final AuthService authService;

    /**
     * 用户微信登录
     *
     * @param code 微信登录code
     * @return token
     */
    @GetMapping("/wxLogin")
    @SystemLog(BusinessName = "Wxlogin")
    public ResponseResult wxlogin(@NotBlank String code) {
        WxLoginUserDetailBo detailBo = weiXinFeignClient.wxLoginUserDetail(code).getData();
        if (Objects.isNull(detailBo.getOpenid())) {
            ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR);
        }
        return authService.wxlogin(detailBo.getOpenid());
    }


    /**
     * 退出登录
     *
     * @return void
     */
    @PostMapping("/logout")
    @SystemLog(BusinessName = "logout")
    public ResponseResult logout() {
        return authService.logout();
    }

    /**
     * 对access_token进行续签
     *
     * @param request 用于获取请求头中的refresh_token
     * @return
     */
    @PostMapping("/refresh")
    @SystemLog(BusinessName = "refreshToken")
    public ResponseResult refreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("refresh_token");
        return authService.refreshAccessToken(refreshToken);
    }

    /**
     * 用户端UID密码登录
     * @param passwordLoginDto
     * @return token
     */
    @PostMapping("/passwordLogin")
    @SystemLog(BusinessName = "passwordLogin")
    public ResponseResult passwordLogin(@Valid @RequestBody PasswordLoginDto passwordLoginDto){

        if(!StringUtils.hasText(passwordLoginDto.getUid())){
            //提示 必须要传UID
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return authService.passwordLogin(passwordLoginDto);
    }
}
