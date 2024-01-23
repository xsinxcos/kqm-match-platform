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
public abstract class AbstractMessageHandler implements MessageHandler {
    //异步保存聊天数据
    protected final ApplicationEventPublisher messageEventPublisher;
    //使用Redis对离线消息进行存储
    protected final RedisCache redisCache;

}
