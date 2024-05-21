package com.chaos.staticResource.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-03-18 22:12
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminAddRotatingPicDto {
    @NotBlank(message = "轮播图不能为空")
    private String name;

    @NotBlank(message = "轮播图URL不能为空")
    private String url;
}
