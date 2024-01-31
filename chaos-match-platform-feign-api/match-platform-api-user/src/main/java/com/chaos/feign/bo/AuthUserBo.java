package com.chaos.feign.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户表(AuthUser)表实体类
 *
 * @author makejava
 * @since 2024-01-10 21:58:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthUserBo {
    //主键ID@TableId
    private Long id;

    //用户名
    private String userName;
    //密码
    private String password;

    private String openid;
    //手机号码
    private String phoneNumber;
    //地址
    private String address;
    //用户头像
    private String avatar;
    //0为普通用户 1为管理员
    private Integer type;
    //性别 0为未知 1为男 2为女
    private Integer sex;
    //状态 1:启用 0:禁用
    private Integer status;


}

