package com.chaos.server;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.chaos.async.event.MessageReceiveEvent;
import com.chaos.async.event.MessageSendEvent;
import com.chaos.bo.MessageBo;
import com.chaos.constant.AppHttpCodeEnum;
import com.chaos.exception.SystemException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: websocket会话控制端
 * @author: xsinxcos
 * @create: 2024-01-23 01:35
 **/
@Component
@Slf4j
@Service
@ServerEndpoint("/chat/socket/{sid}")
public class WebSocketServer {
    //使用ConcurrentHashMap来保证线程安全
    private static Map<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    //与某个客户端的连接对话
    private Session session;
    //接收SID
    private String sid = "";
    private static ApplicationEventPublisher messageEventPublisher;

    @Autowired
    public void setMessageEventPublisher(ApplicationEventPublisher applicationEventPublisher){
        messageEventPublisher = applicationEventPublisher;
    }


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        this.session = session;
        this.sid = sid;
        //将webSocket加入其中
        webSocketMap.put(sid, this);     //加入set中

        try {
            sendMessage("conn_success");
            log.info("用户:+ " + sid + "的websocket链接建立成功");
        } catch (IOException e) {
            log.error("websocket IO Exception");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketMap.remove(sid);  //从map中删除

        //断开连接情况下，更新主板占用情况为释放
        log.info("释放的sid为：" + sid);

    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @ Param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        MessageBo messagebo = JSON.parseObject(message, MessageBo.class);
        //异步持久化发送消息
        messageEventPublisher.publishEvent(new MessageSendEvent(messagebo));
        WebSocketServer server = webSocketMap.get(messagebo.getSendTo().toString());
        try {
            if (Objects.nonNull(server)) {
                server.sendMessage(messagebo.getContent());
                messageEventPublisher.publishEvent(new MessageReceiveEvent(messagebo));
                log.info(sid + "消息发送成功" + "消息内容为" + message);

            } else {
                //todo 转为离线消息
            }
        } catch (IOException e) {
            log.error("sid为" + sid + "的链接发送消息失败");
            throw new SystemException(AppHttpCodeEnum.MESSAGE_SEND_FAIL);
        }
    }

    /**
     * @ Param session
     * @ Param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


}
