package com.chaos.async.event;

import com.chaos.bo.MessageBo;
import org.springframework.context.ApplicationEvent;

/**
 * @description: 消息接收事件
 * @author: xsinxcos
 * @create: 2024-01-23 05:36
 **/
public class MessageReceiveEvent extends ApplicationEvent {
    /**
     * 接收信息
     */
    MessageBo messageBo;

    public MessageReceiveEvent(MessageBo source) {
        super(source);
        this.messageBo = source;
    }
    public MessageBo getMessageBo(){
        return messageBo;
    }
}
