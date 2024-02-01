package com.chaos.strategy;

import com.alibaba.fastjson.JSON;
import com.chaos.async.event.MessageEvent;
import com.chaos.async.event.OfflineMessageEvent;
import com.chaos.constants.MessageConstants;
import com.chaos.domain.bo.MessageBo;
import com.chaos.domain.entity.MessageInfo;
import com.chaos.enums.MessageTypeEnum;
import com.chaos.server.WebSocketServer;
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
@RequiredArgsConstructor
public class DefaultMessageHandlerStrategy extends AbstractMessageHandlerStrategy {
    //异步保存聊天数据到数据库、异步保存离线消息到redis
    private final ApplicationEventPublisher messageEventPublisher;

    @Override
    public void handleMessage(MessageInfo messageInfo, WebSocketServer from, WebSocketServer to, Integer type) throws IOException {
        //利用雪花算法生成uuid
        Long snowFlakeId = SnowFlakeUtil.getDefaultSnowFlakeId();
        messageInfo.setUuid(snowFlakeId);
        MessageBo messageBo = new MessageBo(type, messageInfo);
        //异步持久化发送消息
        messageEventPublisher.publishEvent(new MessageEvent(messageBo));

        if (Objects.nonNull(to)) {
            to.sendMessage(JSON.toJSONString(
                    new MessageBo(MessageTypeEnum.MESSAGE_SEND_NORMAL.getType(), messageInfo)));
        }

        //转为离线消息异步存入Redis，防止消息丢失
        messageEventPublisher.publishEvent(new OfflineMessageEvent(messageBo));

        //todo 消息重发（主要解决消息丢失，及时重发）

        log.debug(from.getSid() + "消息发送成功" + "消息内容为" + messageInfo);
        //发送ack消息通知发送者
        from.sendMessage(JSON.toJSONString(
                MessageBo.builder()
                        .type(MessageTypeEnum.MESSAGE_SEND_ACK.getType())
                        .message(new MessageInfo(null, null, null,
                                MessageConstants.MessageStatusConstants.MESSAGE_SEND_SUCCESS, null,
                                messageInfo.getTimestamp()))
                        .build())
        );
    }
}
