package com.chaos.async.event;

import com.chaos.domain.bo.MessageBo;
import org.springframework.context.ApplicationEvent;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-05-12 20:58
 **/
public class GroupJoinFailMessageEvent extends ApplicationEvent {
    MessageBo messageBo;

    public GroupJoinFailMessageEvent(MessageBo source) {
        super(source);
        this.messageBo = source;
    }

    public MessageBo getGroupJoinFailMessage(){
        return messageBo;
    }
}
