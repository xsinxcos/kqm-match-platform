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
    WX_OPENID("openId" ,"WxOpenIdStrategy");

    private final String type;

    private final String value;

    public static String getValueByType(String grantType){
        for(GrantTypeEnum grantTypeEnum : values()){
            if(grantTypeEnum.getType().equals(grantType)){
                return grantTypeEnum.getValue();
            }
        }
        return null;
    }
}
