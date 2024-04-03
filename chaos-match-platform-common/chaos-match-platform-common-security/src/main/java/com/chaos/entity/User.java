package com.chaos.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表(AuthUser)表实体类
 *
 * @author makejava
 * @since 2024-01-10 21:58:51
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
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
    //状态 0:启用 1:禁用
    private Integer status;
    //创建者
    private Long createBy;
    //创建时间
    private Date createTime;
    //更新者
    private Long updateBy;
    //更新时间
    private Date updateTime;
    //删除标记 默认为0，删除为-1
    private Integer delFlag;


}

