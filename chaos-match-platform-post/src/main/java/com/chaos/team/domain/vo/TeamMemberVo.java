package com.chaos.team.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : wzq
 * @since : 2024-05-19 22:28
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeamMemberVo {
    private String userName;

    private Long id;

    private String avatar;

    private String selfLabel;
}
