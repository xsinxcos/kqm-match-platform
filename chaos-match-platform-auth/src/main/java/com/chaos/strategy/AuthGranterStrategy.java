package com.chaos.strategy;

import com.chaos.entity.AuthParam;
import com.chaos.entity.TokenInfo;

/**
 * @description: 统一登录接口
 * @author: xsinxcos
 * @create: 2024-01-20 23:28
 **/
public interface AuthGranterStrategy {

    /**
     * 登录
     * @param authParam
     * @return
     */
    TokenInfo grant(AuthParam authParam);
}
