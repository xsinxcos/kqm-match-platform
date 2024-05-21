package com.chaos.category.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-05-13 20:56
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryBo {
    private Long id;

    private String name;
}
