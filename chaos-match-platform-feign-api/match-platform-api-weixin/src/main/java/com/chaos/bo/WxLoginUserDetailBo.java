package com.chaos.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxLoginUserDetailBo {
    private String sessionKey;
    private String openid;
    private String unionid;
}
