package com.chaos.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.domain.entity.UserSignin;
import com.chaos.mapper.UserSigninMapper;
import com.chaos.service.UserSigninService;
import org.springframework.stereotype.Service;

/**
 * 用户签到表(UserSignin)表服务实现类
 *
 * @author chaos
 * @since 2024-05-12 02:34:01
 */
@Service("userSigninService")
public class UserSigninServiceImpl extends ServiceImpl<UserSigninMapper, UserSignin> implements UserSigninService {

}

