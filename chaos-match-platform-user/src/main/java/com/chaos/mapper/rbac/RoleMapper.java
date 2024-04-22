package com.chaos.mapper.rbac;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaos.domain.rbac.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author chaos
 * @since 2024-04-22 14:21:52
 */
public interface RoleMapper extends BaseMapper<Role> {
    List<String> selectRoleKeyByUserId(Long id);
}
