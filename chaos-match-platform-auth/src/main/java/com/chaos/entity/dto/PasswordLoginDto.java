package com.chaos.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;

/**
 * @description: 密码登录
 * @author: xsinxcos
 * @create: 2024-03-06 22:24
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordLoginDto {
    //UID（RSA加密后）
    @NotBlank(message = "uid不能为空")
    private String uid;
    //密码（RSA加密后）
    @NotBlank(message = "密码不能为空")
    private String password;
    //RSA公钥
    @NotBlank(message = "公钥不能为空")
    private String publicKey;
}
