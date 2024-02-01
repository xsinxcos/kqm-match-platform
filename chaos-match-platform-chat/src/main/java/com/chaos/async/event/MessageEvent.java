package com.chaos.async.event;

import com.chaos.domain.bo.MessageBo;
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
    MessageBo messageBo;

    public MessageEvent(MessageBo source) {
        super(source);
        this.messageBo = source;
    }

    public MessageBo getMessageSend() {
        return messageBo;
    }
}
