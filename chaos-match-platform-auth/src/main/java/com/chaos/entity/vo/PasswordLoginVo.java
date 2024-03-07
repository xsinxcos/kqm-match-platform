package com.chaos.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 密码登录
 * @author: xsinxcos
 * @create: 2024-03-06 22:24
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordLoginVo {
    private String uid;
    private String password;
}
