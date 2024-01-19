package com.chaos.constant;

public class LoginConstant {
    public final static String USER_REDIS_PREFIX = "appLogin:";
    public final static String ADMIN_REDIS_PREFIX = "adminLogin:";

    public final static Integer REFRESH_TOKEN_TTL = 24 * 60 * 60;

    public final static Integer ACCESS_TOKEN_TTL = 30 * 60;

}
