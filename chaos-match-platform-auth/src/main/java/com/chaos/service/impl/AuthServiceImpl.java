package com.chaos.service.impl;

import cn.hutool.crypto.asymmetric.RSA;
import com.alibaba.fastjson.JSON;
import com.chaos.constant.AppHttpCodeEnum;
import com.chaos.constant.LoginConstant;
import com.chaos.emtity.RSAKey;
import com.chaos.entity.AuthParam;
import com.chaos.entity.LoginUser;
import com.chaos.entity.TokenInfo;
import com.chaos.entity.dto.PasswordLoginDto;
import com.chaos.entity.vo.RsaKeyVo;
import com.chaos.enums.GrantTypeEnum;
import com.chaos.factory.AuthFactory;
import com.chaos.response.ResponseResult;
import com.chaos.service.AuthService;
import com.chaos.strategy.AuthGranterStrategy;
import com.chaos.util.JwtUtil;
import com.chaos.util.RSAUtils;
import com.chaos.util.RedisCache;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 用户表(AuthUser)表服务实现类
 *
 * @author xsinxcos
 * @since 2024-01-10 21:58:52
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final RedisCache redisCache;

    private final AuthFactory authFactory;

    @Override
    public ResponseResult wxlogin(String openid) {
        //包装成统一登录类型
        AuthParam authParam = AuthParam.builder()
                .openid(openid)
                .build();
        //找到相应策略的类型
        String grantType = GrantTypeEnum.getValueByType("app_openId");
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
            log.info("登录失败");
            //refreshToken超时重新登录
            return ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_REFRESH_FAIL);
        }
        String userKey = claims.getSubject();
        //利用userId重新生成access_token 和 refresh_token
        String accessToken = JwtUtil.createShortToken(userKey);
        refreshToken = JwtUtil.createLongToken(userKey);
        redisCache.expire(userKey,
                LoginConstant.REFRESH_TOKEN_TTL, TimeUnit.SECONDS);

        LoginUser loginUser = JSON.parseObject(redisCache.getCacheObject(userKey) ,LoginUser.class);

        TokenInfo tokenInfo = new TokenInfo(accessToken, refreshToken ,loginUser.getUser().getId());

        return ResponseResult.okResult(tokenInfo);
    }

    @Override
    public ResponseResult adminPasswordLogin(PasswordLoginDto passwordLoginDto) {
        //获取私钥
        String publicKey = passwordLoginDto.getPublicKey();
        String privateKey = redisCache.getCacheObject(publicKey);
        //解密
        RSA rsa = new RSA(privateKey, publicKey);
        String decryptUid = RSAUtils.getDecryptString(passwordLoginDto.getUid(), rsa);
        String decryptPassword = RSAUtils.getDecryptString(passwordLoginDto.getPassword(), rsa);

        //包装成统一登录类型
        AuthParam authParam = AuthParam.builder()
                .uid(Long.valueOf(decryptUid))
                .password(decryptPassword)
                .build();
        //找到相应策略的类型
        String grantType = GrantTypeEnum.getValueByType("admin_password");
        AuthGranterStrategy granterStrategy = authFactory.getGranter(grantType);
        //调用策略
        TokenInfo tokenInfo = granterStrategy.grant(authParam);

        //登录成功，清除RSA密钥对
        redisCache.deleteObject(publicKey);
        return ResponseResult.okResult(tokenInfo);
    }

    @Override
    public ResponseResult adminLogout() {
        //获取token 解析获取userId
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //获取userId
        long userid = loginUser.getUser().getId();
        //删除redis中的用户信息
        redisCache.deleteObject(LoginConstant.ADMIN_REDIS_PREFIX + userid);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult adminRefreshAccessToken(String refreshToken) {
        Claims claims = null;
        //解析获取userId用于续签
        try {
            claims = JwtUtil.parseLongToken(refreshToken);
        } catch (Exception e) {
            //refreshToken超时重新登录
            return ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_REFRESH_FAIL);
        }
        String userKey = claims.getSubject();
        //利用userId重新生成access_token 和 refresh_token
        String accessToken = JwtUtil.createShortToken(userKey);
        refreshToken = JwtUtil.createLongToken(userKey);
        redisCache.expire(userKey,
                LoginConstant.REFRESH_TOKEN_TTL, TimeUnit.SECONDS);

        LoginUser loginUser = JSON.parseObject(redisCache.getCacheObject(userKey) ,LoginUser.class);

        TokenInfo tokenInfo = new TokenInfo(accessToken, refreshToken ,loginUser.getUser().getId());
        return ResponseResult.okResult(tokenInfo);
    }

    @Override
    public ResponseResult createRSAKey() {
        //获取RSA密钥对
        RSAKey keyPair = RSAUtils.getKeyPair(new RSA());
        //时间戳
        long timeStamp = new Date().getTime();
        RsaKeyVo vo = RsaKeyVo.builder()
                .publicKey(keyPair.getPublicKey())
                .timeStamp(timeStamp)
                .build();

        //存入Redis
        redisCache.setCacheObject(keyPair.getPublicKey(), keyPair.getPrivateKey(), RSAUtils.KEY_TTL, RSAUtils.TIME_UNIT);

        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult passwordLogin(PasswordLoginDto passwordLoginDto) {
        //获取私钥
        String publicKey = passwordLoginDto.getPublicKey();
        String privateKey = redisCache.getCacheObject(publicKey);
        //解密
        RSA rsa = new RSA(privateKey, publicKey);

        //UID
        Long decryptUid = null;
        if (Objects.nonNull(passwordLoginDto.getUid())) {
            decryptUid = Long.valueOf(RSAUtils.getDecryptString(passwordLoginDto.getUid(), rsa));
        }
        //邮箱
        String decryptEmail = null;
        if (Objects.nonNull(passwordLoginDto.getEmail())) {
            decryptEmail = RSAUtils.getDecryptString(passwordLoginDto.getEmail(), rsa);
        }

        String decryptPassword = RSAUtils.getDecryptString(passwordLoginDto.getPassword(), rsa);

        //包装成统一登录类型
        AuthParam authParam = AuthParam.builder()
                .uid(decryptUid)
                .email(decryptEmail)
                .password(decryptPassword)
                .build();

        //找到相应策略的类型
        String grantType = GrantTypeEnum.getValueByType("app_password");
        AuthGranterStrategy granterStrategy = authFactory.getGranter(grantType);
        //调用策略
        TokenInfo tokenInfo = granterStrategy.grant(authParam);

        //登录成功，清除RSA密钥对
        redisCache.deleteObject(publicKey);
        return ResponseResult.okResult(tokenInfo);
    }
}

