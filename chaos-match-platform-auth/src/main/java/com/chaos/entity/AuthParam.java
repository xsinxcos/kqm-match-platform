package com.chaos.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthParam {
    //UID
    private Long uid;
    //用户名
    private String username;
    //密码
    private String password;
    //openID
    private String openid;
    //邮箱
    private String email;
}
