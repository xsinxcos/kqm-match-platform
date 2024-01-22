package com.chaos.async.listener;

import com.chaos.async.event.MessageSendEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @description: 接收事件监听者
 * @author: xsinxcos
 * @create: 2024-01-23 05:40
 **/
@Component
@Slf4j
public class MessageReceiveListener implements ApplicationListener<MessageSendEvent> {

    @Async
    @Override
    public void onApplicationEvent(MessageSendEvent event) {
        //todo 异步实现将数据持久到数据库
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        log.info("接收消息持久化成功");
    }
}