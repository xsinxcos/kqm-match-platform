package com.chaos.service.rbac;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaos.domain.rbac.dto.ListMenuDto;
import com.chaos.domain.rbac.entity.Menu;
import com.chaos.domain.rbac.vo.AdminTreeSelectMenuVo;
import com.chaos.domain.rbac.vo.MenuVo;
import com.chaos.response.ResponseResult;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author chaos
 * @since 2024-04-22 14:20:37
 */
public interface MenuService extends IService<Menu> {
    List<String> selectPermsByUserId(Long id);

    List<MenuVo> selectRouterMenuTreeByUserId(Long userId);

    ResponseResult menuList(ListMenuDto listMenuDto);

    ResponseResult addMenu(Menu menu);

    ResponseResult getMenu(Long id);

    ResponseResult updateMenu(Menu menu);

    ResponseResult deleteMenu(Long menuId);

    List<AdminTreeSelectMenuVo> treeSelectMenu();

    ResponseResult roleMenuTreeSelect(Long id);
}

