package com.chaos.team.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author : wzq
 * @since : 2024-05-18 22:16
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifyTeamDetailDto {
    @NotNull
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String icon;
}
