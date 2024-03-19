package com.chaos.domain.vo.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-03-19 19:13
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostTagCountVo {
    private Long tagId;
    private String tagName;
    private Long total;
}
