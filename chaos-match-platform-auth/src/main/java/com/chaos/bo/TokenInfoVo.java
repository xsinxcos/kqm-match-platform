package com.chaos.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenInfoVo {
    //accessToken 用于资源请求，有效期短
    private String access_token;
    //refreshToken 只有用于请求accessToken,有效期长
    private String refresh_token;
}
