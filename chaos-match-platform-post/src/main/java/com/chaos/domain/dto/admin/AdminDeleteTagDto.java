package com.chaos.domain.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-03-18 18:58
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDeleteTagDto {

    @NotNull(message = "Id不能为NULL")
    private Long id;
}
