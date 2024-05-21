package com.chaos.user.domain.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-03-16 17:18
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStatusChangeDto {

    @NotNull
    private Long uid;

    @NotNull
    private Integer status;
}
