package com.chaos.async.event;

import com.chaos.bo.MessageBo;
import org.springframework.context.ApplicationEvent;

/**
 * @description: 消息发送事件
 * @author: xsinxcos
 * @create: 2024-01-23 05:16
 **/
public class MessageSendEvent extends ApplicationEvent {
    /**
     * 接收信息
     */
    MessageBo messageBo;

    public MessageSendEvent(MessageBo source) {
        super(source);
        this.messageBo = source;
    }
    public MessageBo getMessageBo(){
        return messageBo;
    }
}
