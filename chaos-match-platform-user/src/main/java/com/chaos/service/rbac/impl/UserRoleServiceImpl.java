package com.chaos.service.rbac.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.mapper.rbac.UserRoleMapper;
import com.chaos.domain.rbac.entity.UserRole;
import com.chaos.service.rbac.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户和角色关联表(UserRole)表服务实现类
 *
 * @author chaos
 * @since 2024-04-22 14:22:27
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}

