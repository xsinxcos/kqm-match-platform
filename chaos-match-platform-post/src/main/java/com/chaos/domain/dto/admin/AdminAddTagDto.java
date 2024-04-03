package com.chaos.domain.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-03-18 18:53
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminAddTagDto {

    @NotBlank(message = "TAG名称不能为空")
    private String name;

    @NotBlank(message = "TAG的URL不能为空")
    private String url;
}
