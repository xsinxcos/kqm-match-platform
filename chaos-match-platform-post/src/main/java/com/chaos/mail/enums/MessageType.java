package com.chaos.mail.enums;

import com.alibaba.fastjson.JSON;
import com.chaos.mail.domain.entity.GroupApplyMail;
import com.chaos.mail.domain.entity.GroupApplyResponseMail;
import com.chaos.mail.domain.entity.IMail;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author : wzq
 * @since : 2024-05-19 19:10
 **/
@Getter
@AllArgsConstructor
public enum MessageType {
    GROUP_APPLY_MESSAGE_TYPE(1 , GroupApplyMail::new),
    GROUP_RESPONSE_MESSAGE_TYPE(2 , GroupApplyResponseMail::new);

    private final Integer type;

    private final Supplier<IMail> supplier;

    public static Supplier<IMail> getSupplier(Integer type){
        for (MessageType messageType : values()) {
            if(messageType.getType().equals(type)){
                return messageType.getSupplier();
            }
        }
        return null;
    }
}
