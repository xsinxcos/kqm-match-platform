package com.chaos.domain.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-03-18 22:12
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminAddRotatingPicDto {
    private String name;
    private String url;
}
