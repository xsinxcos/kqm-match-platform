package com.chaos.strategy;

import com.chaos.domain.entity.MessageInfo;
import com.chaos.server.WebSocketServer;

/**
 * @description: 匹配消息处理策略
 * @author: xsinxcos
 * @create: 2024-02-02 04:00
 **/
public class MatchMessageHandlerStrategy extends AbstractMessageHandlerStrategy {
    @Override
    public void handleMessage(MessageInfo messageInfo, WebSocketServer from, WebSocketServer to, Integer type) {
        //存入数据库
        //存入redis，待处理
        //发送ack消息
    }
}
