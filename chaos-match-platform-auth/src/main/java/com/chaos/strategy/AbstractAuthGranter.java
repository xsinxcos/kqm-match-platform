package com.chaos.strategy;

import com.chaos.constant.LoginConstant;
import com.chaos.entity.TokenInfo;
import com.chaos.util.JwtUtil;
import org.springframework.stereotype.Component;

/**
 * @description: 抽象登录类
 * @author: xsinxcos
 * @create: 2024-01-20 23:30
 **/
@Component
public abstract class AbstractAuthGranter implements AuthGranterStrategy {
    protected TokenInfo createAppTokenInfoByUserId(String userid) {
        //生成并返回TokenInfo
        return TokenInfo.builder()
                .access_token(JwtUtil.createShortToken(LoginConstant.USER_REDIS_PREFIX + userid))
                .refresh_token(JwtUtil.createLongToken(LoginConstant.USER_REDIS_PREFIX + userid))
                .build();
    }

    protected TokenInfo createAdminTokenInfoByUserId(String userid) {
        //生成并返回TokenInfo
        return TokenInfo.builder()
                .access_token(JwtUtil.createShortToken(LoginConstant.ADMIN_REDIS_PREFIX + userid))
                .refresh_token(JwtUtil.createLongToken(LoginConstant.ADMIN_REDIS_PREFIX + userid))
                .build();
    }

}
