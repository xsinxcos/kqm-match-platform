package com.chaos.group.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-05-10 22:55
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ModifyGroupDetailDto {
    @NonNull
    private Long id;

    @NotBlank
    private String name;

    private String icon;

    private String label;

    private String introduction;

    @NonNull
    private Long categoryId;
}
