package com.chaos.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: 登录策略枚举类
 * @author: xsinxcos
 * @create: 2024-01-20 23:41
 **/
@AllArgsConstructor
@Getter
public enum GrantTypeEnum {
    APP_WX_OPENID("app_openId", "wxOpenIdStrategy"),
    APP_PASSWORD("app_password", "passwordStrategy"),
    ADMIN_PASSWORD("admin_password", "adminPasswordStrategy");

    private final String type;

    private final String value;

    public static String getValueByType(String grantType) {
        for (GrantTypeEnum grantTypeEnum : values()) {
            if (grantTypeEnum.getType().equals(grantType)) {
                return grantTypeEnum.getValue();
            }
        }
        return null;
    }
}
