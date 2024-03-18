package com.chaos.domain.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-03-18 18:53
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminAddTagDto {
    private String name;
    private String url;
}
