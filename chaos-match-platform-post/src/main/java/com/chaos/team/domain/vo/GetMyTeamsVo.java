package com.chaos.team.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : wzq
 * @since : 2024-05-18 22:08
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetMyTeamsVo {
    private Long id;
    private String name;
    private String icon;
}
