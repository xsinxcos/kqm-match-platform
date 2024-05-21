package com.chaos.mail.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author : wzq
 * @since : 2024-05-19 19:41
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HandleGroupApplicationDto {
    @NotNull
    private Long id;

    @NotNull
    private Long groupId;

    @NotNull
    private Long userId;

    @NotNull
    private Integer isRead;
}
