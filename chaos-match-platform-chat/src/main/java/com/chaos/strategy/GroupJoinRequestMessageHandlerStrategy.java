package com.chaos.strategy;

import com.chaos.async.event.GroupJoinRequestMessageEvent;
import com.chaos.async.event.GroupJoinSuccessMessageEvent;
import com.chaos.async.event.MatchRequestMessageEvent;
import com.chaos.async.event.MessageEvent;
import com.chaos.domain.bo.MessageBo;
import com.chaos.domain.entity.MessageInfo;
import com.chaos.server.WebSocketServer;
import com.chaos.util.RedisCache;
import com.chaos.util.SnowFlakeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @description: 社群申请消息处理策略
 * @author: xsinxcos
 * @create: 2024-05-12 20:01
 **/
@Component
@Slf4j
public class GroupJoinRequestMessageHandlerStrategy extends AbstractMessageHandlerStrategy {
    public GroupJoinRequestMessageHandlerStrategy(ApplicationEventPublisher messageEventPublisher, RedisCache redisCache) {
        super(messageEventPublisher, redisCache);
    }

    @Override
    public void handleMessage(MessageInfo messageInfo, WebSocketServer from, WebSocketServer to, Integer type) throws IOException {
        //利用雪花算法生成uuid
        Long snowFlakeId = SnowFlakeUtil.getDefaultSnowFlakeId();
        messageInfo.setUuid(snowFlakeId);
        MessageBo messageBo = new MessageBo(type, messageInfo);
        //todo 将社群申请转化为系统通知
        //异步存入数据库
        messageEventPublisher.publishEvent(new MessageEvent(messageBo));
        //将匹配消息存入redis，待处理
        messageEventPublisher.publishEvent(new GroupJoinRequestMessageEvent(messageBo));
        //发送ack消息
        sendAckMessageToMsgFrom(messageBo, from);
        //消息转发
        sendMessageAndSaveOfflineMessage(messageBo, to);
    }
}
