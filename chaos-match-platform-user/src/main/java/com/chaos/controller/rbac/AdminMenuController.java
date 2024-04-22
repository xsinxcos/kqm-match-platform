package com.chaos.controller.rbac;

import com.chaos.domain.rbac.dto.ListMenuDto;
import com.chaos.domain.rbac.entity.Menu;
import com.chaos.response.ResponseResult;
import com.chaos.service.rbac.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 菜单模块（管理端）
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/menu")
public class AdminMenuController {
    private final MenuService menuService;

    /**
     * 菜单展示
     * @param listMenuDto
     * @return
     */
    @GetMapping("/list")
    public ResponseResult menuList(ListMenuDto listMenuDto) {
        return menuService.menuList(listMenuDto);
    }

    /**
     * 添加新菜单
     * @param menu
     * @return
     */
    @PostMapping
    public ResponseResult addMenu(@RequestBody Menu menu) {
        return menuService.addMenu(menu);
    }

    /**
     * 获取某一菜单
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult getMenu(@PathVariable Long id) {
        return menuService.getMenu(id);
    }

    /**
     * 更新菜单
     * @param menu
     * @return
     */
    @PutMapping
    public ResponseResult updateMenu(@RequestBody Menu menu) {
        return menuService.updateMenu(menu);
    }

    /**
     * 删除菜单
     * @param menuId
     * @return
     */

    @DeleteMapping("{menuId}")
    public ResponseResult deleteMenu(@PathVariable Long menuId) {
        return menuService.deleteMenu(menuId);
    }

    /**
     * 树形显示菜单
     * @return
     */
    @GetMapping("/treeselect")
    public ResponseResult treeSelectMenu() {
        return ResponseResult.okResult(menuService.treeSelectMenu());
    }

    /**
     * 找到角色关联菜单
     * @param id
     * @return
     */
    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult roleMenuTreeSelect(@PathVariable Long id) {
        return menuService.roleMenuTreeSelect(id);
    }
}
