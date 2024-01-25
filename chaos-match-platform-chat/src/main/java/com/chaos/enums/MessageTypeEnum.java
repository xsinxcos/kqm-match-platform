package com.chaos.enums;

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
    MESSAGE_SEND_ACK("ack" ,"ackMessageHandlerStrategy"),
    MESSAGE_SEND_NORMAL("message" ,"defaultMessageHandlerStrategy");
    private final String type;
    private final String value;

    public static String getValueByType(String grantType){
        for(MessageTypeEnum grantTypeEnum : values()){
            if(grantTypeEnum.getType().equals(grantType)){
                return grantTypeEnum.getValue();
            }
        }
        return null;
    }
}
