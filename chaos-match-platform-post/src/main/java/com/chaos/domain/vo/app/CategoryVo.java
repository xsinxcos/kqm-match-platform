package com.chaos.domain.vo.app;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-05-13 21:45
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryVo {
    private Long id;

    private String name;

    //父分类id，如果没有父分类为-1
    private Long pid;
    //描述
    private String description;
}
