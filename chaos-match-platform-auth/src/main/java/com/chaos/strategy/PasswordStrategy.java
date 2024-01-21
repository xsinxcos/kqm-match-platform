package com.chaos.strategy;

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
import org.springframework.stereotype.Component;

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
    @Override
    public TokenInfo grant(AuthParam authParam) {
        //根据username获取信息
        AuthUserBo authUserBo = userFeignClient.getUserByUsername(authParam.getUsername()).getData();
        Optional.ofNullable(authUserBo).orElseThrow(()->new RuntimeException("账号不存在"));
        //根据userId生成token
        LoginUser loginUser = new LoginUser(BeanCopyUtils.copyBean(authUserBo, User.class));
        long userid = loginUser.getUser().getId();
        //生成TokenInfo
        TokenInfo tokenInfo = createTokenInfoByUserId(String.valueOf(userid));
        //将用户信息存入redis
        redisCache.setCacheObject(LoginConstant.USER_REDIS_PREFIX + userid, loginUser ,
                LoginConstant.REFRESH_TOKEN_TTL , TimeUnit.SECONDS);
        return tokenInfo;
    }
}
