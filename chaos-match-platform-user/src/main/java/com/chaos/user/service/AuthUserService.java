package com.chaos.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaos.user.domain.dto.UserInfoDto;
import com.chaos.user.domain.dto.admin.EditAccessRightsDto;
import com.chaos.user.domain.dto.admin.UserListDto;
import com.chaos.user.domain.dto.admin.UserStatusChangeDto;
import com.chaos.user.domain.dto.app.PasswordForgetDto;
import com.chaos.user.domain.dto.app.VerificationCodeDto;
import com.chaos.user.domain.dto.app.UserRegisterDto;
import com.chaos.user.domain.entity.AuthUser;
import com.chaos.response.ResponseResult;


/**
 * 用户表(AuthUser)表服务接口
 *
 * @author makejava
 * @since 2024-01-13 06:22:18
 */
public interface AuthUserService extends IService<AuthUser> {

    ResponseResult getUserInfo();

    ResponseResult updateUserInfo(UserInfoDto userInfoDto);

    ResponseResult getUserInfoById(Long userId);

    ResponseResult resetPasswordById(String uid);

    ResponseResult userList(UserListDto userListDto);

    ResponseResult ChangeUserStatus(UserStatusChangeDto userStatusChangeDto);

    ResponseResult editAccessRights(EditAccessRightsDto editAccessRightsDto);

    ResponseResult register(UserRegisterDto userRegisterDto);

    ResponseResult sendRegisterCodeToEmail(VerificationCodeDto dto);

    ResponseResult sendPWResetCodeToEmail(VerificationCodeDto dto);

    ResponseResult forgetPassword(PasswordForgetDto dto);

    ResponseResult checkEmailExist(String email);
}

