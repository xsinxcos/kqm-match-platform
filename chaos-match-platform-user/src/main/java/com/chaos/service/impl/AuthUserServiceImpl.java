package com.chaos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.config.vo.PageVo;
import com.chaos.mapper.AuthUserMapper;
import com.chaos.model.dto.UserInfoDto;
import com.chaos.model.vo.admin.UserListVo;
import com.chaos.model.entity.AuthUser;
import com.chaos.response.ResponseResult;
import com.chaos.service.AuthUserService;
import com.chaos.util.BeanCopyUtils;
import com.chaos.util.SecurityUtils;
import com.chaos.model.vo.UserInfoVo;
import com.chaos.model.dto.admin.EditAccessRightsDto;
import com.chaos.model.dto.admin.UserListDto;
import com.chaos.model.dto.admin.UserStatusChangeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 用户表(AuthUser)表服务实现类
 *
 * @author xsinxcos
 * @since 2024-01-13 06:22:18
 */
@Service("authUserService")
@RequiredArgsConstructor
public class AuthUserServiceImpl extends ServiceImpl<AuthUserMapper, AuthUser> implements AuthUserService {

    private final PasswordEncoder passwordEncoder;

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
        user.setSex(userInfoDto.getSex());
        updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUserInfoById(Long userId) {
        AuthUser user = getById(userId);
        UserInfoVo vo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult resetPasswordById(String uid) {
        //重置生成新密码
        String randomPassword = createRandomPassword();
       //加密新密码
        String encode = passwordEncoder.encode(randomPassword);
        //获取用户
        AuthUser byId = getById(uid);
        //更新新密码
        byId.setPassword(encode);
        updateById(byId);
        //返回响应
        Map<String ,String> map = new HashMap<>();
        map.put("resetPassword" ,randomPassword);

        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult userList(UserListDto userListDto) {
        LambdaQueryWrapper<AuthUser> wrapper = new LambdaQueryWrapper<>();
        //条件筛选
        wrapper.like(Objects.nonNull(userListDto.getUserName()) ,AuthUser::getUserName ,userListDto.getUserName())
                .eq(Objects.nonNull(userListDto.getType()) ,AuthUser::getType ,userListDto.getType())
                .eq(Objects.nonNull(userListDto.getUid()) ,AuthUser::getId ,userListDto.getUid())
                .orderByAsc(AuthUser::getId);
        //分页
        Page<AuthUser> page = new Page<>(userListDto.getPageNum(), userListDto.getPageSize());
        page(page ,wrapper);

        //包装VOS
        List<UserListVo> vos = new ArrayList<>();
        for (AuthUser record : page.getRecords()) {
            UserListVo vo = UserListVo.builder()
                    .uid(record.getId())
                    .sex(record.getSex())
                    .userName(record.getUserName())
                    .avatar(record.getAvatar())
                    .type(record.getType())
                    .phoneNumber(record.getPhoneNumber())
                    .status(record.getStatus())
                    .build();
            vos.add(vo);
        }

        return ResponseResult.okResult(new PageVo(vos ,page.getTotal()));
    }

    @Override
    public ResponseResult ChangeUserStatus(UserStatusChangeDto userStatusChangeDto) {
        AuthUser byId = getById(userStatusChangeDto.getUid());
        byId.setStatus(userStatusChangeDto.getStatus());
        updateById(byId);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult editAccessRights(EditAccessRightsDto editAccessRightsDto) {
        AuthUser byId = getById(editAccessRightsDto.getUid());
        byId.setType(editAccessRightsDto.getType());
        updateById(byId);
        return ResponseResult.okResult();
    }


    private String createRandomPassword() {
        try {
            // 获取当前日期
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sdf.format(new Date());

            // 使用SHA-256算法加密日期
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(date.getBytes());

            // 将加密结果转换为十六进制字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            String encryptedDate = sb.toString();

            // 输出加密结果的前8个字符
            return encryptedDate.substring(0, 8);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("注册失败");
        }
    }
}

