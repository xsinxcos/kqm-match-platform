package com.chaos.domain.rbac.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddRoleDto {
    private String roleName;
    private String roleKey;
    private Integer roleSort;
    private String status;
    private Long[] menuIds;
    private String remark;
}
