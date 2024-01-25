package com.chaos.strategy;

import com.chaos.entity.MessageInfo;
import com.chaos.server.WebSocketServer;
import com.chaos.util.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;


/**
 * @description: websocket消息发送响应处理策略
 * @author: xsinxcos
 * @create: 2024-01-24 01:51
 **/
@Component
@Slf4j
public class AckMessageHandlerStrategy extends AbstractMessageHandlerStrategy {

    public AckMessageHandlerStrategy(ApplicationEventPublisher messageEventPublisher, RedisCache redisCache) {
        super(messageEventPublisher);
    }

    @Override
    public void handleMessage(MessageInfo messageInfo, WebSocketServer from, WebSocketServer to) {

    }
}
