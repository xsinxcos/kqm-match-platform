package com.chaos.async.event;

import com.chaos.domain.entity.Message;
import org.springframework.context.ApplicationEvent;

/**
 * @description: 离线消息事件
 * @author: xsinxcos
 * @create: 2024-01-25 21:10
 **/
public class OfflineMessageEvent extends ApplicationEvent {
    Message message;

    public OfflineMessageEvent(Message source) {
        super(source);
        this.message = source;
    }

    public Message getOffineMessage() {
        return message;
    }
}
