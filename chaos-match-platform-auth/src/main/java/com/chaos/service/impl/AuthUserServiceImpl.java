package com.chaos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.config.util.JwtUtil;
import com.chaos.constant.AppHttpCodeEnum;
import com.chaos.entity.AuthUser;
import com.chaos.entity.LoginUser;
import com.chaos.exception.SystemException;
import com.chaos.mapper.AuthUserMapper;
import com.chaos.response.ResponseResult;
import com.chaos.service.AuthUserService;
import com.chaos.util.RedisCache;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * 用户表(AuthUser)表服务实现类
 *
 * @author makejava
 * @since 2024-01-10 21:58:52
 */
@Service("authUserService")
@RequiredArgsConstructor
public class AuthUserServiceImpl extends ServiceImpl<AuthUserMapper, AuthUser> implements AuthUserService {
    private final RedisCache redisCache;

    @Override
    public ResponseResult wxlogin(String openid) {

        //判断OPENID未存在则存入数据库(第一次登录)
        AuthUser authUser = checkOpenIDExist(openid);
        if(Objects.isNull(authUser)){
            authUser = new AuthUser();
            authUser.setOpenid(openid);
            baseMapper.insert(authUser);
        }
        //根据openID 生成token
        LoginUser loginUser = new LoginUser(authUser);
        String jwt = JwtUtil.createJWT(openid);
        //将用户信息存入redis
        redisCache.setCacheObject("login:" + openid, loginUser);
        Map<String ,String> map = new TreeMap<>();
        map.put("token" ,jwt);
        return ResponseResult.okResult(map);
    }

    private AuthUser checkOpenIDExist(String openID){
        LambdaQueryWrapper<AuthUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Objects.nonNull(openID) ,AuthUser::getOpenid ,openID);
        return baseMapper.selectOne(wrapper);
    }
}

