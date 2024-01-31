package com.chaos.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.mapper.AuthUserMapper;
import com.chaos.model.dto.UserInfoDto;
import com.chaos.model.entity.AuthUser;
import com.chaos.response.ResponseResult;
import com.chaos.service.AuthUserService;
import com.chaos.util.BeanCopyUtils;
import com.chaos.util.SecurityUtils;
import com.chaos.vo.UserInfoVo;
import org.springframework.stereotype.Service;

/**
 * 用户表(AuthUser)表服务实现类
 *
 * @author xsinxcos
 * @since 2024-01-13 06:22:18
 */
@Service("authUserService")
public class AuthUserServiceImpl extends ServiceImpl<AuthUserMapper, AuthUser> implements AuthUserService {

    @Override
    public ResponseResult getUserInfo() {
        Long id = SecurityUtils.getLoginUser().getUser().getId();
        AuthUser user = getById(id);
        UserInfoVo vo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult updateUserInfo(UserInfoDto userInfoDto) {
        Long userId = SecurityUtils.getUserId();
        AuthUser user = getById(userId);
        user.setUserName(userInfoDto.getUserName());
        user.setAvatar(userInfoDto.getAvatar());
        user.setPhoneNumber(userInfoDto.getPhoneNumber());
        updateById(user);
        return ResponseResult.okResult();
    }
}

