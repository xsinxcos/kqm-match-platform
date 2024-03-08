package com.chaos.strategy.admin;

import com.alibaba.fastjson.JSON;
import com.chaos.constant.LoginConstant;
import com.chaos.entity.AuthParam;
import com.chaos.entity.LoginUser;
import com.chaos.entity.TokenInfo;
import com.chaos.feign.UserFeignClient;
import com.chaos.strategy.AbstractAuthGranter;
import com.chaos.util.RedisCache;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @description: 账号密码登录
 * @author: xsinxcos
 * @create: 2024-01-21 00:51
 **/
@Component
@RequiredArgsConstructor
public class AdminPasswordStrategy extends AbstractAuthGranter {
    private final UserFeignClient userFeignClient;

    private final RedisCache redisCache;
    private final AuthenticationManager authenticationManager;

    private final static Integer USER_ADMIN_TYPE = 1;

    @Override

    public TokenInfo grant(AuthParam authParam) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authParam.getUid().toString(), authParam.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断是否认证通过
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("密码错误");
        }
        //根据userId生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();

        //如果不是管理员则拒绝登录
        if (!USER_ADMIN_TYPE.equals(loginUser.getUser().getType())){
            throw new RuntimeException("非管理员账号");
        }

        long userid = loginUser.getUser().getId();
        //生成TokenInfo
        TokenInfo tokenInfo = createAdminTokenInfoByUserId(String.valueOf(userid));
        //将用户信息存入redis
        redisCache.setCacheObject(LoginConstant.ADMIN_REDIS_PREFIX + userid, JSON.toJSONString(loginUser),
                LoginConstant.REFRESH_TOKEN_TTL, TimeUnit.SECONDS);
        return tokenInfo;
    }
}
