package com.chaos.domain.rbac.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListRoleDto {
    private Integer pageNum;
    private Integer pageSize;
    private String roleName;

    private String status;
}
