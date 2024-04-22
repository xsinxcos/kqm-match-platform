package com.chaos.mapper.rbac;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaos.domain.rbac.entity.Menu;

import java.util.List;


/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author chaos
 * @since 2024-04-22 14:20:20
 */
public interface MenuMapper extends BaseMapper<Menu> {
    List<String> selectPermsByUserId(Long id);

    List<Menu> selectAllRouterMenu();

    List<Menu> selectRouterMenuTreeByUserId(Long userId);
}
