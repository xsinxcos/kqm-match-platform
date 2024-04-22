package com.chaos.domain.rbac.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminRoleMenuTreeVo {
    private List<AdminTreeSelectMenuVo> menus;
    private Long[] checkedKeys;
}
