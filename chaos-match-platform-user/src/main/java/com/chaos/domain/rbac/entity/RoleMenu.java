package com.chaos.domain.rbac.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色和菜单关联表(RoleMenu)表实体类
 *
 * @author chaos
 * @since 2024-04-22 14:22:06
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_role_menu")
public class RoleMenu {
    //角色ID@TableId
    private Long roleId;
    //菜单ID@TableId
    private Long menuId;


}

