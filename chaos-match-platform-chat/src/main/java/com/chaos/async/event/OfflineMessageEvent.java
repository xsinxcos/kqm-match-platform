package com.chaos.async.event;

import com.chaos.domain.bo.MessageBo;
import org.springframework.context.ApplicationEvent;

/**
 * @description: 离线消息事件
 * @author: xsinxcos
 * @create: 2024-01-25 21:10
 **/
public class OfflineMessageEvent extends ApplicationEvent {
    MessageBo messageBo;

    public OfflineMessageEvent(MessageBo source) {
        super(source);
        this.messageBo = source;
    }

    public MessageBo getOfflineMessage() {
        return messageBo;
    }
}
