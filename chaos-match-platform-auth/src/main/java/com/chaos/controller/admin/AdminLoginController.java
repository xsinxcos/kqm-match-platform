package com.chaos.controller.admin;

import com.chaos.annotation.SystemLog;
import com.chaos.constant.AppHttpCodeEnum;
import com.chaos.entity.dto.PasswordLoginDto;
import com.chaos.exception.SystemException;
import com.chaos.response.ResponseResult;
import com.chaos.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-03-08 11:18
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/manage")
public class AdminLoginController {
    private final AuthService authService;

    /**
     * 管理端UID密码登录
     * @param passwordLoginDto
     * @return token
     */
    @PostMapping("/passwordLogin")
    @SystemLog(BusinessName = "adminPasswordLogin")
    public ResponseResult adminPasswordLogin(@Valid  @RequestBody PasswordLoginDto passwordLoginDto){

        if(!StringUtils.hasText(passwordLoginDto.getUid())){
            //提示 必须要传UID
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return authService.adminPasswordLogin(passwordLoginDto);
    }

    /**
     * 管理端退出登录
     *
     * @return void
     */
    @PostMapping("/logout")
    @SystemLog(BusinessName = "adminLogout")
    public ResponseResult adminLogout() {
        return authService.adminLogout();
    }


    /**
     * 管理端access_token进行续签
     *
     * @param request 用于获取请求头中的refresh_token
     * @return
     */
    @PostMapping("/refresh")
    @SystemLog(BusinessName = "adminRefreshToken")
    public ResponseResult adminRefreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("refresh_token");
        return authService.adminRefreshAccessToken(refreshToken);
    }


    /**
     * 获取RSA密钥对
     * @return
     */
    @PostMapping("/getKey")
    @SystemLog(BusinessName = "createRSAKey")
    public ResponseResult adminCreateRSAKey(){
        return authService.createRSAKey();
    }
}
