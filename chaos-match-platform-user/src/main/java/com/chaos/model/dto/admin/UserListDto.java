package com.chaos.model.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-03-16 17:03
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListDto {
    @NotNull
    private Integer pageNum;

    @NotNull
    private Integer pageSize;

    private Integer type;

    private String userName;

    private Long uid;
}
