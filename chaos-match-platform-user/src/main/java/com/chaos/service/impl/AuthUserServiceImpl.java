package com.chaos.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.entity.AuthUser;
import com.chaos.mapper.AuthUserMapper;
import com.chaos.service.AuthUserService;
import org.springframework.stereotype.Service;

/**
 * 用户表(AuthUser)表服务实现类
 *
 * @author makejava
 * @since 2024-01-13 06:22:18
 */
@Service("authUserService")
public class AuthUserServiceImpl extends ServiceImpl<AuthUserMapper, AuthUser> implements AuthUserService {

}

