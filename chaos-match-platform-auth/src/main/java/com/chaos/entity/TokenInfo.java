package com.chaos.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenInfo {
    //accessToken 用于资源请求，有效期短
    private String access_token;
    //refreshToken 只有用于请求accessToken,有效期长
    private String refresh_token;
    //用户的userID
    private Long userId;
}
