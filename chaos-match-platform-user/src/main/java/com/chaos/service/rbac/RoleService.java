package com.chaos.service.rbac;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaos.domain.rbac.dto.AddRoleDto;
import com.chaos.domain.rbac.dto.ChangeRoleStatusDto;
import com.chaos.domain.rbac.dto.ListRoleDto;
import com.chaos.domain.rbac.dto.RoleUpdateDto;
import com.chaos.domain.rbac.entity.Role;
import com.chaos.response.ResponseResult;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author chaos
 * @since 2024-04-22 14:21:52
 */
public interface RoleService extends IService<Role> {
    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult listRole(ListRoleDto listRoleDto);

    ResponseResult changeRoleStatus(ChangeRoleStatusDto changeRoleStatusDto);

    ResponseResult showRole(Long id);

    ResponseResult addRole(AddRoleDto addRoleDto);


    ResponseResult updateRole(RoleUpdateDto roleUpdateDto);

    ResponseResult deleteRole(Long id);

    ResponseResult listAllRole();
}

