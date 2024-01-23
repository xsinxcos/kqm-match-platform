package com.chaos.strategy;

import com.alibaba.fastjson.JSON;
import com.chaos.async.event.MessageReceiveEvent;
import com.chaos.async.event.MessageSendEvent;
import com.chaos.constants.MessageStatusConstants;
import com.chaos.entity.Message;
import com.chaos.entity.MessageInfo;
import com.chaos.entity.MessageReceive;
import com.chaos.entity.MessageSend;
import com.chaos.enums.MessageTypeEnum;
import com.chaos.server.WebSocketServer;
import com.chaos.util.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

/**
 * @description: 正常消息发送策略
 * @author: xsinxcos
 * @create: 2024-01-24 02:59
 **/
@Component
@Slf4j
public class DefaultMessageHandler extends AbstractMessageHandler {
    public DefaultMessageHandler(ApplicationEventPublisher messageEventPublisher, RedisCache redisCache) {
        super(messageEventPublisher, redisCache);
    }

    @Override
    public void handleMessage(MessageInfo messageInfo, WebSocketServer from, WebSocketServer to) throws IOException {

        //异步持久化发送消息
        messageEventPublisher.publishEvent(new MessageSendEvent(
                MessageSend.builder()
                        .msgFrom(messageInfo.getSendFrom())
                        .msgTo(messageInfo.getSendTo())
                        .content(messageInfo.getContent())
                        .build()
        ));

        if (Objects.nonNull(to)) {
            to.sendMessage(JSON.toJSONString(
                    new Message(MessageTypeEnum.MESSAGE_SEND_NORMAL.getType(), messageInfo)));
        } else {
            //转为离线消息异步持久化
            messageEventPublisher.publishEvent(new MessageReceiveEvent(
                    MessageReceive.builder()
                            .msgFrom(messageInfo.getSendFrom())
                            .msgTo(messageInfo.getSendTo())
                            .delivered(MessageStatusConstants.MESSAGE_UNDELIVERED)
                            .build()
            ));
        }

        log.info(to.getSid() + "消息发送成功" + "消息内容为" + messageInfo);
        //发送ack消息会发送者
        from.sendMessage(JSON.toJSONString(
                Message.builder()
                        .type(MessageTypeEnum.MESSAGE_SEND_ACK.getType())
                        .message(new MessageInfo(null, null, MessageStatusConstants.MESSAGE_SEND_SUCCESS,
                                new Date().getTime()))
                        .build())
        );
    }
}
