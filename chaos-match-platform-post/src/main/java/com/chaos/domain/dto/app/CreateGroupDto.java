package com.chaos.domain.dto.app;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-05-09 21:12
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateGroupDto {

    @NotBlank
    private String name;

    private String icon;

    private String label;

    private String introduction;

    @NonNull
    private Long categoryId;
}
