package com.chaos.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaos.model.dto.UserInfoDto;
import com.chaos.model.dto.admin.EditAccessRightsDto;
import com.chaos.model.dto.admin.UserListDto;
import com.chaos.model.dto.admin.UserStatusChangeDto;
import com.chaos.model.dto.app.UserRegisterDto;
import com.chaos.model.entity.AuthUser;
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
}

