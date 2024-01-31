package com.chaos.service.impl;

import com.chaos.constant.AppHttpCodeEnum;
import com.chaos.constant.LoginConstant;
import com.chaos.entity.AuthParam;
import com.chaos.entity.LoginUser;
import com.chaos.entity.TokenInfo;
import com.chaos.enums.GrantTypeEnum;
import com.chaos.factory.AuthFactory;
import com.chaos.feign.UserFeignClient;
import com.chaos.response.ResponseResult;
import com.chaos.service.AuthService;
import com.chaos.strategy.AuthGranterStrategy;
import com.chaos.util.JwtUtil;
import com.chaos.util.RedisCache;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 用户表(AuthUser)表服务实现类
 *
 * @author xsinxcos
 * @since 2024-01-10 21:58:52
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final RedisCache redisCache;

    private final UserFeignClient userFeignClient;

    private final AuthFactory authFactory;

    @Override
    public ResponseResult wxlogin(String openid) {
        //包装成统一登录类型
        AuthParam authParam = AuthParam.builder()
                .openid(openid)
                .build();
        //找到相应策略的类型
        String grantType = GrantTypeEnum.getValueByType("openId");
        AuthGranterStrategy granterStrategy = authFactory.getGranter(grantType);
        //调用策略
        TokenInfo tokenInfo = granterStrategy.grant(authParam);
        return ResponseResult.okResult(tokenInfo);
    }

    @Override
    public ResponseResult logout() {
        //获取token 解析获取userId
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //获取userId
        long userid = loginUser.getUser().getId();
        //删除redis中的用户信息
        redisCache.deleteObject(LoginConstant.USER_REDIS_PREFIX + userid);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult refreshAccessToken(String refreshToken) {
        Claims claims = null;
        //解析获取userId用于续签
        try {
            claims = JwtUtil.parseLongToken(refreshToken);
        } catch (Exception e) {
            e.printStackTrace();
            //refreshToken超时重新登录
            return ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_REFRESH_FAIL);
        }
        String userId = claims.getSubject();
        //利用userId重新生成access_token 和 refresh_token
        String accessToken = JwtUtil.createShortToken(userId);
        refreshToken = JwtUtil.createLongToken(userId);
        redisCache.expire(LoginConstant.USER_REDIS_PREFIX + userId,
                LoginConstant.REFRESH_TOKEN_TTL, TimeUnit.SECONDS);
        TokenInfo tokenInfo = new TokenInfo(accessToken, refreshToken);
        return ResponseResult.okResult(tokenInfo);
    }
}

