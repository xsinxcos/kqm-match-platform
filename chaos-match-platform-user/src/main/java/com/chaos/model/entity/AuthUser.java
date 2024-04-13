package com.chaos.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户表(AuthUser)表实体类
 *
 * @author makejava
 * @since 2024-01-13 06:22:17
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_auth_user")
public class AuthUser {
    //主键ID@TableId
    private Long id;

    //用户名
    private String userName;
    //密码
    private String password;
    //邮箱
    private String email;
    //微信OpenId
    private String openid;
    //手机号码
    private String phoneNumber;
    //用户头像
    private String avatar;
    //0为普通用户 1为管理员
    private Integer type;
    //性别 0为未知 1为男 2为女
    private Integer sex;
    //状态 0:启用 1:禁用
    private Integer status;
    //创建者
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //更新者
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;
    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;


}

