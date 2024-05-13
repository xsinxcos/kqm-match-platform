package com.chaos.domain.dto.app;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-05-13 01:22
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListGroupDto {
    private Integer pageNum;

    private Integer pageSize;

    private Integer categoryId;
}
