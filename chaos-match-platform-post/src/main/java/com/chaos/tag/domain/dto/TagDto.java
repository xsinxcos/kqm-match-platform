package com.chaos.tag.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-02-19 16:40
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDto {
    @NotNull(message = "id不能为NULL")
    private Long id;

    @NotBlank(message = "名称不能为空")
    private String name;
}
