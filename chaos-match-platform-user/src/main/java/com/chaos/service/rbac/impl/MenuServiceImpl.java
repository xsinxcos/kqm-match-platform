package com.chaos.service.rbac.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.constant.AppHttpCodeEnum;
import com.chaos.domain.rbac.dto.ListMenuDto;
import com.chaos.domain.rbac.entity.RoleMenu;
import com.chaos.domain.rbac.vo.*;
import com.chaos.exception.SystemException;
import com.chaos.mapper.rbac.MenuMapper;
import com.chaos.domain.rbac.entity.Menu;
import com.chaos.response.ResponseResult;
import com.chaos.service.rbac.MenuService;
import com.chaos.service.rbac.RoleMenuService;
import com.chaos.util.BeanCopyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author chaos
 * @since 2024-04-22 14:20:23
 */
@Service("menuService")
@RequiredArgsConstructor
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    private final RoleMenuService roleMenuService;

    public static final String MENU = "C";

    public static final String BUTTON = "F";

    public static final Integer STATUS_NORMAL = 1;


    @Override
    public List<String> selectPermsByUserId(Long id) {
        //如果是管理员返回全部权限
        if(id == 1L){
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Menu::getMenuType , MENU,BUTTON);
            queryWrapper.eq(Menu::getStatus ,STATUS_NORMAL);
            List<Menu> menus = list();
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }
        //否则返回所具有的权限
        return getBaseMapper().selectPermsByUserId(id);

    }

    @Override
    public List<MenuVo> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
         menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menus ,MenuVo.class);
        List<MenuVo> menuTree = builderMenuTree(menuVos ,0L);

        return menuTree;
    }

    @Override
    public ResponseResult menuList(ListMenuDto listMenuDto) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper();
//        可以针对菜单名进行模糊查询
//  	也可以针对菜单的状态进行查询
//  	菜单要按照父菜单id和orderNum进行排序
        if(!Objects.isNull(listMenuDto.getStatus())) {
            wrapper.eq(Menu::getStatus, listMenuDto.getStatus());
        }
        if(!Objects.isNull(listMenuDto.getMenuName())) {
            wrapper.like(Menu::getMenuName, listMenuDto.getMenuName());
        }
        wrapper.orderByAsc( Menu::getParentId,Menu::getOrderNum);
        List<Menu> menus = list(wrapper);
        //进行封装Vo转化
        List<AdminMenuListVo> adminMenuListVos = BeanCopyUtils.copyBeanList(menus, AdminMenuListVo.class);
        return ResponseResult.okResult(adminMenuListVos);
    }

    @Override
    public ResponseResult addMenu(Menu menu) {
        save(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getMenu(Long id) {
        Menu menu = getById(id);
        AdminUpdateMenuVo adminUpdateMenuVo = BeanCopyUtils.copyBean(menu, AdminUpdateMenuVo.class);
        return ResponseResult.okResult(adminUpdateMenuVo);
    }

    @Override
    public ResponseResult updateMenu(Menu menu) {
        if(menu.getParentId().equals(menu.getId())){
            throw new SystemException(AppHttpCodeEnum.MENU_PARENT_ERROR);
        }
        updateById(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteMenu(Long menuId) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId ,menuId);
        List<Menu> list = list(wrapper);
        if(!list.isEmpty()){
            throw new SystemException(AppHttpCodeEnum.SUBMENU_EXIST);
        }
        getBaseMapper().deleteById(menuId);
        return ResponseResult.okResult();
    }

    private List<MenuVo> builderMenuTree(List<MenuVo> menuVos, long parentId) {
        List<MenuVo> MenuTree = MenuTree = new ArrayList<>();
        for(MenuVo menuVo : menuVos){
            if(menuVo.getParentId().equals(parentId)){
                menuVo.setChildren(getChildren(menuVos ,menuVo.getId()));
                MenuTree.add(menuVo);
            }
        }
        return MenuTree;
    }

    private List<MenuVo> getChildren(List<MenuVo> menuVos, long parentId) {
        List<MenuVo> MenuTree = new ArrayList<>();
        for(MenuVo menuVo : menuVos){
            if(menuVo.getParentId().equals(parentId)){
                menuVo.setChildren(getChildren(menuVos ,menuVo.getId()));
                MenuTree.add(menuVo);
            }
        }
        if(MenuTree.isEmpty()) return null;
        return MenuTree;
    }

    @Override
    public List<AdminTreeSelectMenuVo> treeSelectMenu() {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Menu::getParentId,Menu::getOrderNum);
        List<Menu> menus = list(wrapper);
        List<AdminTreeSelectMenuVo> vos = allMenuTree(menus ,0L);
        return vos;
    }

    private List<AdminTreeSelectMenuVo> allMenuTree(List<Menu> menus ,long parentId){
        List<AdminTreeSelectMenuVo> MenuTree = new ArrayList<>();
        for(Menu menu : menus){
            if(menu.getParentId().equals(parentId)){
                MenuTree.add(new AdminTreeSelectMenuVo(menu.getId() ,menu.getMenuName() ,menu.getParentId(),
                        allMenuTree(menus ,menu.getId())));
            }
        }
        if(MenuTree.isEmpty()) return null;
        return MenuTree;
    }

    @Override
    public ResponseResult roleMenuTreeSelect(Long id) {
        AdminRoleMenuTreeVo vo = new AdminRoleMenuTreeVo();
        //建立菜单树
        List<AdminTreeSelectMenuVo> vos = treeSelectMenu();
        vo.setMenus(vos);
        //查找角色所关联的菜单权限id列表
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId ,id);
        List<RoleMenu> list = roleMenuService.list(wrapper);
        List<Long> MenuIds = new ArrayList<>();
        for(RoleMenu roleMenu: list){
            MenuIds.add(roleMenu.getMenuId());
        }
        vo.setCheckedKeys(MenuIds.toArray(new Long[0]));
        return ResponseResult.okResult(vo);
    }
}

