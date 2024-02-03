package com.chaos.async.event;

import com.chaos.domain.bo.MessageBo;
import org.springframework.context.ApplicationEvent;

/**
 * @description: 匹配消息事件
 * @author: xsinxcos
 * @create: 2024-02-04 00:57
 **/
public class MatchRequestMessageEvent extends ApplicationEvent {
    MessageBo messageBo;

    public MatchRequestMessageEvent(MessageBo source) {
        super(source);
        this.messageBo = source;
    }

    public MessageBo getMatchMessage() {
        return messageBo;
    }
}
