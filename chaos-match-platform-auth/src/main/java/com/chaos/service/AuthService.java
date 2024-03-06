package com.chaos.service;

import com.chaos.entity.vo.PasswordLoginVo;
import com.chaos.response.ResponseResult;


/**
 * 用户表(AuthUser)表服务接口
 *
 * @author makejava
 * @since 2024-01-10 21:58:52
 */
public interface AuthService {

    ResponseResult wxlogin(String openid);

    ResponseResult logout();

    ResponseResult refreshAccessToken(String refreshToken);

    ResponseResult passwordLogin(PasswordLoginVo passwordLoginVo);
}

