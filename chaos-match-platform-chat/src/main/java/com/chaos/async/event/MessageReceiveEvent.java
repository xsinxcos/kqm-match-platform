package com.chaos.async.event;

import com.chaos.entity.MessageInfo;
import com.chaos.entity.MessageReceive;
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
    MessageReceive message;

    public MessageReceiveEvent(MessageReceive source) {
        super(source);
        this.message = source;
    }
    public MessageReceive getMessageInfo(){
        return message;
    }
}
