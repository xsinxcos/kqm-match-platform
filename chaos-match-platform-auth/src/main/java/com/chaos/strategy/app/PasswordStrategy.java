package com.chaos.strategy.app;

import com.alibaba.fastjson.JSON;
import com.chaos.constant.LoginConstant;
import com.chaos.entity.AuthParam;
import com.chaos.entity.LoginUser;
import com.chaos.entity.TokenInfo;
import com.chaos.entity.User;
import com.chaos.feign.UserFeignClient;
import com.chaos.feign.bo.AuthUserBo;
import com.chaos.handler.authenticationToken.EmailPasswordAuthenticationToken;
import com.chaos.response.ResponseResult;
import com.chaos.strategy.AbstractAuthGranter;
import com.chaos.util.BeanCopyUtils;
import com.chaos.util.RedisCache;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @description: uid密码登录
 * @author: xsinxcos
 * @create: 2024-04-11 18:32
 **/
@Component
@RequiredArgsConstructor
public class PasswordStrategy extends AbstractAuthGranter {

    private final RedisCache redisCache;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;
    private final static Integer USER_ADMIN_TYPE = 1;

    private final UserFeignClient userFeignClient;

    @Override
    public TokenInfo grant(AuthParam authParam) {
        LoginUser loginUser = null;

        if(Objects.nonNull(authParam.getUid())){
            loginUser = uidGrant(authParam.getUid() ,authParam.getPassword());
        }else if(Objects.nonNull(authParam.getEmail())){
            loginUser = emailGrant(authParam.getEmail() ,authParam.getPassword());
        }

        long userid = loginUser.getUser().getId();
        //生成TokenInfo
        TokenInfo tokenInfo = createAdminTokenInfoByUserId(String.valueOf(userid));
        //将用户信息存入redis
        redisCache.setCacheObject(LoginConstant.ADMIN_REDIS_PREFIX + userid, JSON.toJSONString(loginUser),
                LoginConstant.REFRESH_TOKEN_TTL, TimeUnit.SECONDS);


        return tokenInfo;
    }

    private LoginUser uidGrant(Long uid ,String password){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(uid, password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断是否认证通过
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("密码错误");
        }
        //判断是否认证通过
        return (LoginUser) authenticate.getPrincipal();
    }

    private LoginUser emailGrant(String email ,String password){
        EmailPasswordAuthenticationToken authenticationToken = new EmailPasswordAuthenticationToken(email ,password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断是否认证通过
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("密码错误");
        }
        //判断是否认证通过
        return (LoginUser) authenticate.getPrincipal();
    }
}
