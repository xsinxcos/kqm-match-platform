package com.chaos.domain.user.dto.app;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @description: 用户注册
 * @author: xsinxcos
 * @create: 2024-04-13 20:11
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDto {
    @NotBlank(message = "用户名不能为空")
    private String userName;

    @NotBlank
    private String email;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank
    private String publicKey;

    @NotBlank
    private String code;
}
