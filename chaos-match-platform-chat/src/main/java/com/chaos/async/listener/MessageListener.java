package com.chaos.async.listener;

import com.chaos.async.event.MessageEvent;
import com.chaos.domain.bo.MessageBo;
import com.chaos.domain.entity.Message;
import com.chaos.domain.entity.MessageInfo;
import com.chaos.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @description: 发送消息监听
 * @author: xsinxcos
 * @create: 2024-01-23 05:22
 **/
@Component
@Slf4j
@RequiredArgsConstructor
public class MessageListener implements ApplicationListener<MessageEvent> {
    private final MessageService messageService;

    @Async("asyncExecutor")
    @Override
    public void onApplicationEvent(MessageEvent event) {
        MessageBo messageSend = event.getMessageSend();
        MessageInfo messageInfo = messageSend.getMessage();
        Message message = Message.builder()
                .type(messageSend.getType())
                .msgTo(messageInfo.getSendTo())
                .msgFrom(messageInfo.getSendFrom())
                .content(messageInfo.getContent())
                .sendTime(new Date(messageInfo.getTimestamp()))
                .uuid(messageInfo.getUuid()).build();
        //异步实现将数据持久到数据库
        messageService.save(message);
        log.info("发送消息持久化成功");
    }


}
