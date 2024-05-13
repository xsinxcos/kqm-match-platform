package com.chaos.strategy;

import com.alibaba.fastjson.JSON;
import com.chaos.async.event.OfflineMessageEvent;
import com.chaos.constants.MessageConstants;
import com.chaos.domain.bo.MessageBo;
import com.chaos.domain.entity.MessageInfo;
import com.chaos.server.WebSocketServer;
import com.chaos.strategy.enums.MessageTypeEnum;
import com.chaos.util.RedisCache;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

/**
 * @description: 消息处理抽象类
 * @author: xsinxcos
 * @create: 2024-01-24 03:05
 **/
@Component
@RequiredArgsConstructor
public abstract class AbstractMessageHandlerStrategy implements MessageHandlerStrategy {
    //异步保存聊天数据到数据库，处理redis中的数据
    protected final ApplicationEventPublisher messageEventPublisher;

    protected final RedisCache redisCache;

    /**
     * 发送ack应答
     *
     * @param messageBo
     * @param from
     */

    protected void sendAckMessageToMsgFrom(MessageBo messageBo, WebSocketServer from) {
        MessageInfo messageInfo = messageBo.getMessage();
        try {
            from.sendMessage(JSON.toJSONString(
                    MessageBo.builder()
                            .type(MessageTypeEnum.MESSAGE_SEND_ACK.getType())
                            .message(MessageInfo.builder()
                                    .uuid(messageInfo.getUuid())
                                    .content(MessageConstants.MessageStatusConstants.MESSAGE_SEND_SUCCESS)
                                    .timestamp(messageInfo.getTimestamp())
                                    .build())
                            .build())
            );
        } catch (IOException e) {
            throw new RuntimeException("USERID为：" + from.getSid() + " ,内容为" + JSON.toJSON(messageBo) + "的ack消息发送失败");
        }
    }

    /**
     * 消息转发，并且存入离线消息防止消息丢失
     *
     * @param messageBo
     * @param to
     * @throws IOException
     */
    protected void sendMessageAndSaveOfflineMessage(MessageBo messageBo, WebSocketServer to) throws IOException {
        if (Objects.nonNull(to)) {
            to.sendMessage(JSON.toJSONString(messageBo));
        }
        //转为离线消息异步存入Redis和数据库，防止消息丢失
        messageEventPublisher.publishEvent(new OfflineMessageEvent(messageBo));
    }
}
