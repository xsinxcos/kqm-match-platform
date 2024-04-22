package com.chaos.domain.user.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoVo {
    private String userName;
    private Integer sex;
    private String phoneNumber;
    private String avatar;
}
