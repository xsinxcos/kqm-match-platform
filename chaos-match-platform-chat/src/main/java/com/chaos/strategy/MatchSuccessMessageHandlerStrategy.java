package com.chaos.strategy;

import com.chaos.async.event.MatchSuccessMessageEvent;
import com.chaos.async.event.MessageEvent;
import com.chaos.domain.bo.MessageBo;
import com.chaos.domain.entity.MessageInfo;
import com.chaos.server.WebSocketServer;
import com.chaos.util.RedisCache;
import com.chaos.util.SnowFlakeUtil;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @description: 匹配成功消息处理策略
 * @author: xsinxcos
 * @create: 2024-02-02 04:04
 **/
@Component
public class MatchSuccessMessageHandlerStrategy extends AbstractMessageHandlerStrategy {
    public MatchSuccessMessageHandlerStrategy(ApplicationEventPublisher messageEventPublisher, RedisCache redisCache) {
        super(messageEventPublisher, redisCache);
    }

    @Override
    public void handleMessage(MessageInfo messageInfo, WebSocketServer from, WebSocketServer to, Integer type) throws IOException {
        //利用雪花算法生成uuid
        Long snowFlakeId = SnowFlakeUtil.getDefaultSnowFlakeId();
        messageInfo.setUuid(snowFlakeId);
        MessageBo messageBo = new MessageBo(type, messageInfo);
        //异步存入数据库
        messageEventPublisher.publishEvent(new MessageEvent(messageBo));
        //处理Redis中存放的请求
        messageEventPublisher.publishEvent(new MatchSuccessMessageEvent(messageBo));
        //发送ack消息
        //发送ack消息通知发送者
        sendAckMessageToMsgFrom(messageBo, from);
        //消息转发
        sendMessageAndSaveOfflineMessage(messageBo, to);
    }
}
