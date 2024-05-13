package com.chaos.async.event;

import com.chaos.domain.bo.MessageBo;
import org.springframework.context.ApplicationEvent;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-05-12 20:05
 **/
public class GroupJoinRequestMessageEvent extends ApplicationEvent {
    MessageBo messageBo;

    public GroupJoinRequestMessageEvent(MessageBo source) {
        super(source);
        this.messageBo = source;
    }

    public MessageBo getGroupJoinRequestMessage(){
        return messageBo;
    }
}
