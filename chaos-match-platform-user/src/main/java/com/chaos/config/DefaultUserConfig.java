package com.chaos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @description: 用户默认初始信息
 * @author: xsinxcos
 * @create: 2024-04-25 00:55
 **/
@Component
public class DefaultUserConfig {
    //默认用户名

    public static String defaultUsername;

    //默认头像

    public static String defaultAvatar;


    @Value("${user.default.avatar}")
    public void setDefaultAvatar(String defaultAvatar) {
        DefaultUserConfig.defaultAvatar = defaultAvatar;
    }
}
