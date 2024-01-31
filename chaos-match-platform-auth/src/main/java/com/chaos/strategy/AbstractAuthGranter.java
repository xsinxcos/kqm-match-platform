package com.chaos.strategy;

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
    protected TokenInfo createTokenInfoByUserId(String userid) {
        //生成并返回TokenInfo
        return TokenInfo.builder()
                .access_token(JwtUtil.createShortToken(String.valueOf(userid)))
                .refresh_token(JwtUtil.createLongToken(String.valueOf(userid)))
                .build();
    }

}
