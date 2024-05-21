package com.chaos.mail.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author : wzq
 * @since : 2024-05-19 16:38
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplyGroupDto {
    @NotNull
    private Long groupId;
}
