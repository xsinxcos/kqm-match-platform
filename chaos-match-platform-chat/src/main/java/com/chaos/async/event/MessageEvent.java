package com.chaos.async.event;

import com.chaos.entity.Message;
import org.springframework.context.ApplicationEvent;

/**
 * @description: 消息发送事件
 * @author: xsinxcos
 * @create: 2024-01-23 05:16
 **/
public class MessageEvent extends ApplicationEvent {
    /**
     * 接收信息
     */
    Message message;

    public MessageEvent(Message source) {
        super(source);
        this.message = source;
    }

    public Message getMessageSend() {
        return message;
    }
}
