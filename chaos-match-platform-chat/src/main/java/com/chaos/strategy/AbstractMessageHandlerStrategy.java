package com.chaos.strategy;

import com.chaos.util.RedisCache;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * @description: 消息处理抽象类
 * @author: xsinxcos
 * @create: 2024-01-24 03:05
 **/
@Component
@RequiredArgsConstructor
public abstract class AbstractMessageHandlerStrategy implements MessageHandlerStrategy {
    //异步保存聊天数据到数据库、异步保存离线消息到redis
    protected final ApplicationEventPublisher messageEventPublisher;

}
