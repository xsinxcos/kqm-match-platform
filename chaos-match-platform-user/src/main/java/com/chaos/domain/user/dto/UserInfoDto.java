package com.chaos.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {
    private String userName;
    private Integer sex;
    private String phoneNumber;
    private String avatar;
    private String selfLabel;
}
