package com.chaos.domain.vo.app;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-05-14 11:49
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupListVo {
    private Long id;

    private String name;

    private String icon;

    private String label;

    private String introduction;

    private Long membersCount;
}
