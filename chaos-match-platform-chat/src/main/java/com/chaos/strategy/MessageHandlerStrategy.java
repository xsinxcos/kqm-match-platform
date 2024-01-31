package com.chaos.strategy;

import cn.hutool.extra.spring.SpringUtil;
import com.chaos.domain.bo.MessageBo;
import com.chaos.domain.entity.MessageInfo;
import com.chaos.enums.MessageTypeEnum;
import com.chaos.server.WebSocketServer;
import org.springframework.beans.factory.ListableBeanFactory;

import java.io.IOException;
import java.util.Objects;

/**
 * @description: 消息策略接口
 * @author: xsinxcos
 * @create: 2024-01-24 03:01
 **/
public interface MessageHandlerStrategy {
    static void handleMessage(MessageBo messageBo, WebSocketServer from, WebSocketServer to) throws IOException {
        ListableBeanFactory beanFactory = SpringUtil.getBeanFactory();
        String type = MessageTypeEnum.getValueByType(messageBo.getType());

        if (!beanFactory.containsBean(Objects.requireNonNull(type))) {
            throw new RuntimeException("未找到此种类型");
        }

        MessageHandlerStrategy handler = (MessageHandlerStrategy) beanFactory.getBean(type);
        handler.handleMessage(messageBo.getMessage() ,from ,to);
    }

    void handleMessage(MessageInfo messageInfo, WebSocketServer from, WebSocketServer to) throws IOException;
}
