package com.chaos.strategy.app;

import com.alibaba.fastjson.JSON;
import com.chaos.constant.LoginConstant;
import com.chaos.entity.AuthParam;
import com.chaos.entity.LoginUser;
import com.chaos.entity.TokenInfo;
import com.chaos.entity.User;
import com.chaos.feign.UserFeignClient;
import com.chaos.feign.bo.AuthUserBo;
import com.chaos.strategy.AbstractAuthGranter;
import com.chaos.util.BeanCopyUtils;
import com.chaos.util.RedisCache;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private final PasswordEncoder passwordEncoder;

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
            //随机生成密码
            authUserBo.setPassword(createRandomPassword());
            authUserBo = userFeignClient.addUserByOpenId(openid).getData();
        }
        if (isLocked(authUserBo)) {
            throw new RuntimeException("该账号已被冻结");
        }
        //根据openID 生成token
        LoginUser loginUser = new LoginUser(BeanCopyUtils.copyBean(authUserBo, User.class) ,null);
        long userid = loginUser.getUser().getId();
        //生成TokenInfo
        TokenInfo tokenInfo = createAppTokenInfoByUserId(String.valueOf(userid));
        //将用户信息存入redis
        redisCache.setCacheObject(LoginConstant.USER_REDIS_PREFIX + userid, JSON.toJSONString(loginUser),
                LoginConstant.REFRESH_TOKEN_TTL, TimeUnit.SECONDS);
        return tokenInfo;
    }

    private String createRandomPassword() {
        try {
            // 获取当前日期
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(new Date());

            // 使用SHA-256算法加密日期
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(date.getBytes());

            // 将加密结果转换为十六进制字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            String encryptedDate = sb.toString();

            // 输出加密结果的前8个字符，密码进行加密
            return passwordEncoder.encode(encryptedDate.substring(0, 8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("注册失败");
        }
    }
}
