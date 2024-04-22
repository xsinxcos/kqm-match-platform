package com.chaos.domain.user.dto.app;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-04-19 20:19
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PasswordForgetDto {

    @NotBlank
    private String email;

    @NotBlank
    private String newPassword;

    @NotBlank
    private String code;

    @NotBlank
    private String publicKey;
}
