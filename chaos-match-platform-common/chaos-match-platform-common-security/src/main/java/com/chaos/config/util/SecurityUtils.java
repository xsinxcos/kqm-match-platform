package com.chaos.config.util;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils
{


    /**
     * 获取用户
     **/
    public static <V> V getLoginUser(Class<V> clazz)
    {
        return (V) getAuthentication().getPrincipal();
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

}