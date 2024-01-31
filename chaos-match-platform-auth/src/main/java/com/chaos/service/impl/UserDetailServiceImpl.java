package com.chaos.service.impl;

import com.chaos.entity.LoginUser;
import com.chaos.entity.User;
import com.chaos.feign.UserFeignClient;
import com.chaos.feign.bo.AuthUserBo;
import com.chaos.util.BeanCopyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserFeignClient userFeignClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        AuthUserBo user = userFeignClient.getUserByUsername(username).getData();
        //判断是否查到用户，如果没有查到抛出异常
        if (Objects.isNull(user)) {
            throw new RuntimeException("用户不存在");
        }
        //返回用户信息
        //todo 权限封装
//        if(user.getType().equals(SystemConstants.ADMIN)){
//            List<String> list = menuMapper.selectPermsByUserId(user.getId());
//            return new LoginUser(user ,list);
//        }
        return new LoginUser(BeanCopyUtils.copyBean(user, User.class));
    }

}
