package com.chaos.user.domain.dto.app;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-04-18 22:15
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VerificationCodeDto {

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "不是一个合法的电子邮件地址")
    private String email;
}
