package com.chaos.util;

import com.chaos.entity.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    private static final String ANONYMOUS_USER = "anonymousUser";

    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser() {
        Object principal = getAuthentication().getPrincipal();
        if (ANONYMOUS_USER.equals(principal.toString())) {
            return null;
        }
        return (LoginUser) getAuthentication().getPrincipal();
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }


    public static Long getUserId() {
        return getLoginUser().getUser().getId();
    }
}