package com.chaos.controller.rbac;

import com.chaos.domain.rbac.dto.AddRoleDto;
import com.chaos.domain.rbac.dto.ChangeRoleStatusDto;
import com.chaos.domain.rbac.dto.ListRoleDto;
import com.chaos.domain.rbac.dto.RoleUpdateDto;
import com.chaos.service.rbac.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.chaos.response.ResponseResult;

/**
 * 角色模块（管理端）
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/role")
public class AdminRoleController {

    private final RoleService roleService;

    /**
     * 展示所有角色
     * @param listRoleDto
     * @return
     */
    @GetMapping("/list")
    public ResponseResult listRole(ListRoleDto listRoleDto) {
        return roleService.listRole(listRoleDto);
    }

    /**
     * 改变角色状态
     * @param changeRoleStatusDto
     * @return
     */

    @PutMapping("/changeStatus")
    public ResponseResult changeRoleStatus(@RequestBody ChangeRoleStatusDto changeRoleStatusDto) {
        return roleService.changeRoleStatus(changeRoleStatusDto);
    }

    /**
     * 添加角色
     * @param addRoleDto
     * @return
     */

    @PostMapping
    public ResponseResult addRole(@RequestBody AddRoleDto addRoleDto) {
        return roleService.addRole(addRoleDto);
    }

    /**
     *  显示某一角色信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult showRole(@PathVariable Long id) {
        return roleService.showRole(id);
    }

    /**
     * 更新某一角色
     * @param roleUpdateDto
     * @return
     */
    @PutMapping
    public ResponseResult updateRole(@RequestBody RoleUpdateDto roleUpdateDto) {
        return roleService.updateRole(roleUpdateDto);
    }

    /**
     * 删除角色
     * @param id
     * @return
     */

    @DeleteMapping("/{id}")
    public ResponseResult deleteRole(@PathVariable Long id) {
        return roleService.deleteRole(id);
    }

    /**
     * 展示所有角色
     * @return
     */
    @GetMapping("/listAllRole")
    public ResponseResult listAllRole() {
        return roleService.listAllRole();
    }
}
