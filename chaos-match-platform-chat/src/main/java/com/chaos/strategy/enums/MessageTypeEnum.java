package com.chaos.strategy.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: 消息类型枚举类
 * @author: xsinxcos
 * @create: 2024-01-24 01:16
 **/
@AllArgsConstructor
@Getter
public enum MessageTypeEnum {
    MESSAGE_SEND_ACK(999, "ackMessageHandlerStrategy"),
    MESSAGE_SEND_NORMAL(0, "defaultMessageHandlerStrategy"),
    MESSAGE_MATCH(1, "matchRequestMessageHandlerStrategy"),
    MESSAGE_MATCH_SUCCESS(2, "matchSuccessMessageHandlerStrategy"),
    MESSAGE_MATCH_FAIL(3, "matchFailMessageHandlerStrategy");
    private final Integer type;
    private final String value;

    public static String getValueByType(Integer grantType) {
        for (MessageTypeEnum grantTypeEnum : values()) {
            if (grantTypeEnum.getType().equals(grantType)) {
                return grantTypeEnum.getValue();
            }
        }
        return null;
    }
}
