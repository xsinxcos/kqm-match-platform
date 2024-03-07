package com.chaos.strategy;

import com.alibaba.fastjson.JSON;
import com.chaos.constant.LoginConstant;
import com.chaos.entity.AuthParam;
import com.chaos.entity.LoginUser;
import com.chaos.entity.TokenInfo;
import com.chaos.entity.User;
import com.chaos.feign.UserFeignClient;
import com.chaos.feign.bo.AuthUserBo;
import com.chaos.util.BeanCopyUtils;
import com.chaos.util.RedisCache;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @description: 账号密码登录
 * @author: xsinxcos
 * @create: 2024-01-21 00:51
 **/
@Component
@RequiredArgsConstructor
public class PasswordStrategy extends AbstractAuthGranter {
    private final UserFeignClient userFeignClient;

    private final RedisCache redisCache;
    private final AuthenticationManager authenticationManager;

    @Override
    public TokenInfo grant(AuthParam authParam) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authParam.getUid().toString(), authParam.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断是否认证通过
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("密码错误");
        }
        //根据userId生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        long userid = loginUser.getUser().getId();
        //生成TokenInfo
        TokenInfo tokenInfo = createTokenInfoByUserId(String.valueOf(userid));
        //将用户信息存入redis
        redisCache.setCacheObject(LoginConstant.USER_REDIS_PREFIX + userid, JSON.toJSONString(loginUser),
                LoginConstant.REFRESH_TOKEN_TTL, TimeUnit.SECONDS);
        return tokenInfo;
    }
}
