package com.chaos.async.event;

import com.chaos.entity.Message;
import com.chaos.entity.MessageInfo;
import com.chaos.entity.MessageSend;
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
    MessageSend message;

    public MessageSendEvent(MessageSend source) {
        super(source);
        this.message = source;
    }
    public MessageSend getMessageSend(){
        return message;
    }
}
