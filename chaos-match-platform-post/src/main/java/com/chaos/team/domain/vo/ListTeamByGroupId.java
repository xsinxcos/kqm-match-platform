package com.chaos.team.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : wzq
 * @since : 2024-05-16 00:47
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListTeamByGroupId {
    private Long id;
    private String name;
    private String icon;
}
