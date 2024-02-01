package com.chaos.strategy;

import com.chaos.domain.entity.MessageInfo;
import com.chaos.server.WebSocketServer;

import java.io.IOException;

/**
 * @description: 匹配失败消息处理策略
 * @author: xsinxcos
 * @create: 2024-02-02 04:03
 **/
public class MatchFailMessageHandlerStrategy extends AbstractMessageHandlerStrategy {
    @Override
    public void handleMessage(MessageInfo messageInfo, WebSocketServer from, WebSocketServer to, Integer type) throws IOException {
        //存入数据库
        //处理Redis中存放的请求
        //发送ack消息
    }
}
