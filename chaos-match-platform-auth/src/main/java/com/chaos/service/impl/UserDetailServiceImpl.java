package com.chaos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chaos.entity.AuthUser;
import com.chaos.entity.LoginUser;
import com.chaos.mapper.AuthUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final AuthUserMapper authUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<AuthUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AuthUser::getUserName ,username);
        AuthUser user = authUserMapper.selectOne(queryWrapper);
        //判断是否查到用户，如果没有查到抛出异常
        if(Objects.isNull(user)){
            throw new RuntimeException("用户不存在");
        }
        //返回用户信息
        //todo 权限封装
//        if(user.getType().equals(SystemConstants.ADMIN)){
//            List<String> list = menuMapper.selectPermsByUserId(user.getId());
//            return new LoginUser(user ,list);
//        }
        return new LoginUser(user);
    }

}
