package com.chaos.async.event;

import com.chaos.domain.bo.MessageBo;
import lombok.NonNull;
import org.springframework.context.ApplicationEvent;

/**
 * @description: 匹配失败消息处理
 * @author: xsinxcos
 * @create: 2024-02-04 01:12
 **/
public class MatchFailMessageEvent extends ApplicationEvent {
    MessageBo messageBo;

    public MatchFailMessageEvent(MessageBo source) {
        super(source);
        this.messageBo = source;
    }

    public MessageBo getMatchResultMessage() {
        return messageBo;
    }
}
