package com.chaos.controller.admin;

import com.chaos.constant.AppHttpCodeEnum;
import com.chaos.entity.vo.PasswordLoginVo;
import com.chaos.exception.SystemException;
import com.chaos.response.ResponseResult;
import com.chaos.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
     * @param passwordLoginVo
     * @return token
     */

    @PostMapping("/passwordLogin")
    public ResponseResult adminPasswordLogin(@RequestBody PasswordLoginVo passwordLoginVo){
        if(!StringUtils.hasText(passwordLoginVo.getUid())){
            //提示 必须要传UID
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return authService.adminPasswordLogin(passwordLoginVo);
    }

    /**
     * 管理端退出登录
     *
     * @return void
     */
    @PostMapping("/logout")
    public ResponseResult adminLogout() {
        return authService.adminLogout();
    }


    /**
     * 管理端access_token进行续签
     *
     * @param request 用于获取请求头中的refresh_token
     * @return
     */
    @GetMapping("/refresh")
    public ResponseResult adminRefreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("refresh_token");
        return authService.adminRefreshAccessToken(refreshToken);
    }
}
