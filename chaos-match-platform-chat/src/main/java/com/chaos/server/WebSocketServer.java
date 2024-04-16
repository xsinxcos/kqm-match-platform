package com.chaos.server;

import com.alibaba.fastjson.JSON;
import com.chaos.constant.AppHttpCodeEnum;
import com.chaos.constants.MessageConstants;
import com.chaos.domain.bo.MessageBo;
import com.chaos.domain.entity.MessageInfo;
import com.chaos.entity.LoginUser;
import com.chaos.exception.SystemException;
import com.chaos.response.ResponseResult;
import com.chaos.strategy.MessageHandlerStrategy;
import com.chaos.util.JwtUtil;
import com.chaos.util.RedisCache;
import com.chaos.util.SecurityUtils;
import com.chaos.util.WebUtils;
import io.jsonwebtoken.Claims;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: websocket会话控制端
 * @author: xsinxcos
 * @create: 2024-01-23 01:35
 **/
@Component
@Slf4j
@Service
@Getter
@ServerEndpoint("/web/socket")
public class WebSocketServer {
    //使用ConcurrentHashMap来保证线程安全
    private static Map<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    //与某个客户端的连接对话
    private Session session;
    //接收SID
    private String sid = "";
    //异步保存聊天数据
    private static ApplicationEventPublisher messageEventPublisher;
    //使用Redis对离线消息进行存储
    private static RedisCache redisCache;

    @Autowired
    public void setMessageEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        messageEventPublisher = applicationEventPublisher;
    }

    @Autowired
    public void setRedisCache(RedisCache redis) {
        redisCache = redis;
    }


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        String queryString = session.getQueryString();

        if(queryString.isEmpty()){
            closeSession(session ,CloseReason.CloseCodes.CANNOT_ACCEPT ,"未登录");
            throw new RuntimeException("未登录");
        }
        String token = extractParameter(queryString, "access_token");
        Claims claims = null;
        try {
            claims = JwtUtil.parseShortToken(token);
        }catch (Exception e){
            closeSession(session ,CloseReason.CloseCodes.VIOLATED_POLICY ,"access_token过期");
        }
        this.session = session;

        String userKey = claims.getSubject();
        //从redis中获取用户信息
        LoginUser loginUser = JSON.parseObject(redisCache.getCacheObject(
                userKey), LoginUser.class);

        if (Objects.isNull(loginUser)) {
            //说明登录过期  提示重新登录
            closeSession(session ,CloseReason.CloseCodes.CANNOT_ACCEPT ,"请重新登录");
            return;
        }

        //存入SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        this.sid = SecurityUtils.getUserId().toString();
        //将webSocket加入其中
        //加入set中
        webSocketMap.put(sid, this);


        try {
            sendMessage("conn_success");
            log.info("用户id为:" + sid + "的websocket链接建立成功");
        } catch (IOException e) {
            log.error("websocket IO Exception");
        }

        //将离线消息进行推送
        sendBatchOffLineMessage();
    }

    private String extractParameter(String queryString, String parameterName) {
        String regex = parameterName + "=([^&]*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(queryString);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    private void closeSession(Session session ,CloseReason.CloseCodes closeCodes ,String reason){
        try {
            session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY ,"refresh_token过期"));
        } catch (IOException e) {
            throw new RuntimeException("websocket连接关闭失败");
        }
    }
    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        //从map中删除
        webSocketMap.remove(sid);
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
        //最简单的PING PONG机制
        if("ping".equals(message)){
            try {
                this.sendMessage("pong");
            } catch (IOException e) {
                throw new RuntimeException("pong回应失败");
            }
            return;
        }

        //消息检验处理
        MessageBo parseMessageBo = JSON.parseObject(message, MessageBo.class);
        MessageInfo messageInfo = parseMessageBo.getMessage();

        parseMessageBo.getMessage().setSendFrom(Long.parseLong(sid));
        WebSocketServer server = null;

        if (Objects.nonNull(messageInfo.getSendTo())) {
            server = webSocketMap.get(messageInfo.getSendTo().toString());
        }

        try {
            MessageHandlerStrategy.handleMessage(parseMessageBo, this, server);
        } catch (IOException e) {
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

    /**
     * 将离线消息进行推送并删除redis中的离线数据
     */
    public void sendBatchOffLineMessage() {
        String key = MessageConstants.OFFLINE_MESSAGE_REDIS_KEY + sid;
        ZSetOperations<String, String> cacheZSet = redisCache.getCacheZSet();
        Set<String> message = cacheZSet.range(key, 0, -1);
        if (Objects.isNull(message)) {
            return;
        }

        message.forEach(t -> {
            try {
                sendMessage(t);
            } catch (IOException e) {
                throw new SystemException(AppHttpCodeEnum.MESSAGE_SEND_FAIL);
            }
        });
    }
}
