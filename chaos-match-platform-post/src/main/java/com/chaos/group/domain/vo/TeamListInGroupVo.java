package com.chaos.group.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : wzq
 * @since : 2024-05-19 21:42
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeamListInGroupVo {
    private Long id;

    private String name;

    private String icon;

    private Integer teamNumber;
}
