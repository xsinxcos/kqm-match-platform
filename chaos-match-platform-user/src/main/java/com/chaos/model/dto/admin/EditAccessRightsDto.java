package com.chaos.model.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-03-16 17:25
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditAccessRightsDto {
    @NotNull
    private Long uid;

    @NotNull
    private Integer type;
}
