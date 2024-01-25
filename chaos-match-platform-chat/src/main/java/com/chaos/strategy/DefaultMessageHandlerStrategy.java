package com.chaos.strategy;

import com.alibaba.fastjson.JSON;
import com.chaos.async.event.MessageEvent;
import com.chaos.async.event.OfflineMessageEvent;
import com.chaos.bo.MessageBo;
import com.chaos.constants.MessageConstants;
import com.chaos.entity.Message;
import com.chaos.entity.MessageInfo;
import com.chaos.enums.MessageTypeEnum;
import com.chaos.server.WebSocketServer;
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
public class DefaultMessageHandlerStrategy extends AbstractMessageHandlerStrategy {
    public DefaultMessageHandlerStrategy(ApplicationEventPublisher messageEventPublisher) {
        super(messageEventPublisher);
    }

    @Override
    public void handleMessage(MessageInfo messageInfo, WebSocketServer from, WebSocketServer to) throws IOException {

        Message message = Message.builder()
                .msgFrom(messageInfo.getSendFrom())
                .msgTo(messageInfo.getSendTo())
                .content(messageInfo.getContent())
                .sendTime(new Date(messageInfo.getTimestamp()))
                .type(MessageConstants.MessageStatusConstants.MESSAGE_TYPE_TEXT)
                .build();

        //异步持久化发送消息
        messageEventPublisher.publishEvent(new MessageEvent(message));

        if (Objects.nonNull(to)) {
            to.sendMessage(JSON.toJSONString(
                    new MessageBo(MessageTypeEnum.MESSAGE_SEND_NORMAL.getType(), messageInfo)));
        } else {
            //转为离线消息异步存入Redis
            messageEventPublisher.publishEvent(new OfflineMessageEvent(message));
        }

        log.info(from.getSid() + "消息发送成功" + "消息内容为" + messageInfo);
        //发送ack消息通知发送者
        from.sendMessage(JSON.toJSONString(
                MessageBo.builder()
                        .type(MessageTypeEnum.MESSAGE_SEND_ACK.getType())
                        .message(new MessageInfo(null, null,
                                MessageConstants.MessageStatusConstants.MESSAGE_SEND_SUCCESS,
                                new Date().getTime()))
                        .build())
        );
    }
}
