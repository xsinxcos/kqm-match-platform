package com.chaos.domain.rbac.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户和角色关联表(UserRole)表实体类
 *
 * @author chaos
 * @since 2024-04-22 14:22:27
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user_role")
public class UserRole {
    //用户ID@TableId
    private Long userId;
    //角色ID@TableId
    private Long roleId;


}

