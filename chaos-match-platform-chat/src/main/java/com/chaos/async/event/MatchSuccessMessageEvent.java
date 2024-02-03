package com.chaos.async.event;

import com.chaos.domain.bo.MessageBo;
import org.springframework.context.ApplicationEvent;

/**
 * @description: 匹配消息处理事件
 * @author: xsinxcos
 * @create: 2024-02-04 01:00
 **/
public class MatchSuccessMessageEvent extends ApplicationEvent {
    MessageBo messageBo;

    public MatchSuccessMessageEvent(MessageBo source) {
        super(source);
        this.messageBo = source;
    }

    public MessageBo getMatchResultMessage() {
        return messageBo;
    }
}
