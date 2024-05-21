package com.chaos.usersign.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : wzq
 * @since : 2024-05-18 01:25
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SigninRankVo {
    private Long id;

    private String name;

    private String icon;

    private Long count;
}
