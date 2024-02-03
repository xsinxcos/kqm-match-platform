package com.chaos.strategy;

import com.alibaba.fastjson.JSON;
import com.chaos.async.event.MessageEvent;
import com.chaos.async.event.OfflineMessageEvent;
import com.chaos.constants.MessageConstants;
import com.chaos.domain.bo.MessageBo;
import com.chaos.domain.entity.MessageInfo;
import com.chaos.server.WebSocketServer;
import com.chaos.strategy.enums.MessageTypeEnum;
import com.chaos.util.RedisCache;
import com.chaos.util.SnowFlakeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

/**
 * @description: 正常消息发送策略
 * @author: xsinxcos
 * @create: 2024-01-24 02:59
 **/
@Component
@Slf4j
public class DefaultMessageHandlerStrategy extends AbstractMessageHandlerStrategy {
    public DefaultMessageHandlerStrategy(ApplicationEventPublisher messageEventPublisher, RedisCache redisCache) {
        super(messageEventPublisher, redisCache);
    }


    @Override
    public void handleMessage(MessageInfo messageInfo, WebSocketServer from, WebSocketServer to, Integer type) throws IOException {
        //利用雪花算法生成uuid
        Long snowFlakeId = SnowFlakeUtil.getDefaultSnowFlakeId();
        messageInfo.setUuid(snowFlakeId);
        MessageBo messageBo = new MessageBo(type, messageInfo);
        //异步持久化发送消息
        messageEventPublisher.publishEvent(new MessageEvent(messageBo));
        //消息转发
        sendMessageAndSaveOfflineMessage(messageBo ,to);
        //todo 消息重发（主要解决消息丢失，及时重发）

        log.debug(from.getSid() + "消息发送成功" + "消息内容为" + messageInfo);
        //发送ack消息通知发送者
        sendAckMessageToMsgFrom(messageBo ,from);
    }
}
