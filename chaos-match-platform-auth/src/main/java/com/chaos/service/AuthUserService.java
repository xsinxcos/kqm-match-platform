package com.chaos.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaos.entity.AuthUser;
import com.chaos.response.ResponseResult;


/**
 * 用户表(AuthUser)表服务接口
 *
 * @author makejava
 * @since 2024-01-10 21:58:52
 */
public interface AuthUserService extends IService<AuthUser> {

    ResponseResult wxlogin(String openid);

    ResponseResult logout();
}

