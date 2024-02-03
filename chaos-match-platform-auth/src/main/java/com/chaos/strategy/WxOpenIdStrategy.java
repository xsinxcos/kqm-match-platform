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
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @description: 微信登录策略
 * @author: xsinxcos
 * @create: 2024-01-20 23:31
 **/
@Component
@RequiredArgsConstructor
public class WxOpenIdStrategy extends AbstractAuthGranter {

    private final UserFeignClient userFeignClient;
    private final RedisCache redisCache;

    @Override
    public TokenInfo grant(AuthParam authParam) {
        String openid = authParam.getOpenid();
        AuthUserBo authUserBo = userFeignClient.getUserByOpenId(openid).getData();
        //判断OPENID未存在则存入数据库(第一次登录)
        if (Objects.isNull(authUserBo)) {
            authUserBo = new AuthUserBo();
            authUserBo.setOpenid(openid);
            authUserBo = userFeignClient.addUserByOpenId(openid).getData();
        }
        //根据openID 生成token
        LoginUser loginUser = new LoginUser(BeanCopyUtils.copyBean(authUserBo, User.class));
        long userid = loginUser.getUser().getId();
        //生成TokenInfo
        TokenInfo tokenInfo = createTokenInfoByUserId(String.valueOf(userid));
        //将用户信息存入redis
        redisCache.setCacheObject(LoginConstant.USER_REDIS_PREFIX + userid, JSON.toJSONString(loginUser),
                LoginConstant.REFRESH_TOKEN_TTL, TimeUnit.SECONDS);
        return tokenInfo;
    }
}
