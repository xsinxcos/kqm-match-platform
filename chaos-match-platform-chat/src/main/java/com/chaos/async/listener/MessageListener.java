package com.chaos.async.listener;

import com.chaos.async.event.MessageEvent;
import com.chaos.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

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

    @Async
    @Override
    public void onApplicationEvent(MessageEvent event) {
        //异步实现将数据持久到数据库
        messageService.save(event.getMessageSend());
        log.info("发送消息持久化成功");
    }


}
