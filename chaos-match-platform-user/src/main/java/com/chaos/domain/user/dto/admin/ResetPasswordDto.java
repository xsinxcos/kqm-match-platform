package com.chaos.domain.user.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-03-16 17:00
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordDto {
    @NotBlank(message = "uid不能为空")
    private String uid;
}
