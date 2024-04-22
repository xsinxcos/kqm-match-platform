package com.chaos.service.rbac.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.mapper.rbac.RoleMenuMapper;
import com.chaos.domain.rbac.entity.RoleMenu;
import com.chaos.service.rbac.RoleMenuService;
import org.springframework.stereotype.Service;

/**
 * 角色和菜单关联表(RoleMenu)表服务实现类
 *
 * @author chaos
 * @since 2024-04-22 14:22:06
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

}

