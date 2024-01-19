package com.chaos.service.impl;

import com.chaos.bo.TokenInfoVo;
import com.chaos.constant.AppHttpCodeEnum;
import com.chaos.constant.LoginRedisConstant;
import com.chaos.entity.LoginUser;
import com.chaos.entity.User;
import com.chaos.feign.UserFeignClient;
import com.chaos.feign.bo.AuthUserBo;
import com.chaos.response.ResponseResult;
import com.chaos.service.AuthService;
import com.chaos.util.BeanCopyUtils;
import com.chaos.util.JwtUtil;
import com.chaos.util.RedisCache;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
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

    @Override
    public ResponseResult wxlogin(String openid) {
        //判断OPENID未存在则存入数据库(第一次登录)
        AuthUserBo authUserBo = userFeignClient.getUserByOpenId(openid).getData();
        if(Objects.isNull(authUserBo)){
            authUserBo = new AuthUserBo();
            authUserBo.setOpenid(openid);
            userFeignClient.addUserByOpenId(openid);
        }
        //根据openID 生成token
        LoginUser loginUser = new LoginUser(BeanCopyUtils.copyBean(authUserBo, User.class));
        long userid = loginUser.getUser().getId();
        TokenInfoVo vo = new TokenInfoVo();
        vo.setAccess_token(JwtUtil.createShortToken(String.valueOf(userid)));
        vo.setRefresh_token(JwtUtil.createLongToken(String.valueOf(userid)));

        //将用户信息存入redis
        redisCache.setCacheObject(LoginRedisConstant.USER_REDIS_PREFIX + userid, loginUser ,
                LoginRedisConstant.REFRESH_TOKEN_TTL , TimeUnit.SECONDS);

        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult logout() {
        //获取token 解析获取userId
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //获取userId
        long userid = loginUser.getUser().getId();
        //删除redis中的用户信息
        redisCache.deleteObject(LoginRedisConstant.USER_REDIS_PREFIX + userid);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult refreshAccessToken(String refreshToken) {
        Claims claims = null;
        //解析获取userId用于续签
        try {
            claims = JwtUtil.parseJWT(refreshToken);
        } catch (Exception e) {
            e.printStackTrace();
            //refreshToken超时重新登录
            return ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_REFRESH_FAIL);
        }
        String userId = claims.getSubject();
        //利用userId重新生成access_token
        String accessToken = JwtUtil.createShortToken(userId);
        Map<String ,String> map = new TreeMap<>();
        map.put("access_token" ,accessToken);
        return ResponseResult.okResult(map);
    }
}

