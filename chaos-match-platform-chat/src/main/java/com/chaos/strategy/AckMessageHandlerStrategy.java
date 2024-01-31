package com.chaos.strategy;

import com.chaos.constants.MessageConstants;
import com.chaos.domain.entity.MessageInfo;
import com.chaos.server.WebSocketServer;
import com.chaos.util.RedisCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * @description: websocket消息发送响应处理策略
 * @author: xsinxcos
 * @create: 2024-01-24 01:51
 **/
@Component
@Slf4j
@RequiredArgsConstructor
public class AckMessageHandlerStrategy extends AbstractMessageHandlerStrategy {
    private final RedisCache redisCache;

    @Override
    public void handleMessage(MessageInfo messageInfo, WebSocketServer from, WebSocketServer to) {
        String key = MessageConstants.OFFLINE_MESSAGE_REDIS_KEY + from.getSid();
        redisCache.getCacheZSet().removeRangeByScore(key, messageInfo.getUuid(), messageInfo.getUuid());
    }
}
