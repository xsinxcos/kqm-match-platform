package com.chaos.async.event;

import com.chaos.domain.bo.MessageBo;
import org.springframework.context.ApplicationEvent;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-05-12 21:17
 **/
public class GroupJoinSuccessMessageEvent extends ApplicationEvent {
    MessageBo messageBo;

    public GroupJoinSuccessMessageEvent(MessageBo source) {
        super(source);
        this.messageBo = source;
    }

    public MessageBo getGroupJoinSuccessMessage(){
        return messageBo;
    }
}
