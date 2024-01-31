package com.chaos.server;

import com.alibaba.fastjson.JSON;
import com.chaos.domain.XfChatRequest;
import com.chaos.domain.XfChatResponse;
import com.chaos.util.RedisCache;
import com.chaos.util.SecurityUtils;
import com.chaos.utils.XfGPTUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ZSetOperations;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@Getter
public class XfModelServerListener extends WebSocketListener {

    private String totalAnswer;

    public boolean is_finished;

    public final String GPT_HISTORY_MESSAGE_KEY = "GPT:";

    @Resource
    private RedisCache redisCache;


    public XfModelServerListener() {
        totalAnswer = "";
        is_finished = false;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        // System.out.println(userId + "用来区分那个用户的结果" + text);
        XfChatResponse myJsonParse = JSON.parseObject(text, XfChatResponse.class);
        if (myJsonParse.getHeader().getCode() != 0) {
            log.error("发生错误，错误码为：" + myJsonParse.getHeader().getCode());
            log.error("本次请求的sid为：" + myJsonParse.getHeader().getSid());
            webSocket.close(1000, "");
        }
        List<XfChatResponse.Text> textList = myJsonParse.getPayload().getChoices().getText();
        for (XfChatResponse.Text temp : textList) {
            totalAnswer = totalAnswer + temp.getContent();
        }
        if (myJsonParse.getHeader().getStatus() == 2) {
            is_finished = true;
        }
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        try {
            if (null != response) {
                int code = response.code();
                log.error("onFailure code:" + code);
                log.error("onFailure body:" + response.body().string());
                if (101 != code) {
                    System.out.println("connection failed");
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public XfModelServerListener sendQuestion(String question, XfModelServerListener webSocketListener) throws Exception {
        //构建鉴权url
        String authUrl = XfGPTUtils.getAuthUrl();
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        String url = authUrl.toString().replace("http://", "ws://").replace("https://", "wss://");
        //建立websocket请求
        Request request = new Request.Builder().url(url).build();
        WebSocket webSocket = client.newWebSocket(request, webSocketListener);
        //获取历史消息（最近20条）
        String key = GPT_HISTORY_MESSAGE_KEY + SecurityUtils.getUserId();
        List<XfChatRequest.Text> requestTexts = new ArrayList<>(Objects.requireNonNull(getGptHistory(key)));
        //加入新问题
        XfChatRequest.Text newQuestion = new XfChatRequest.Text("user", question);
        insertGptHistory(key, "user", question);
        requestTexts.add(newQuestion);
        //构建请求内容
        XfChatRequest request1 = XfGPTUtils.getRequest(requestTexts);
        //发送请求
        String message = JSON.toJSON(request1).toString();
        is_finished = false;
        webSocket.send(message);

        return webSocketListener;
    }

    private List<XfChatRequest.Text> getGptHistory(String key) {
        ZSetOperations<String, String> cacheZSet = redisCache.getCacheZSet();
        Set<String> range = cacheZSet.range(key, 0, -1);
        if (range != null) {
            List<XfChatRequest.Text> collect = range.stream().
                    map(t -> JSON.parseObject(t, XfChatRequest.Text.class))
                    .collect(Collectors.toList());
            return collect;
        }
        return null;
    }

    public void insertGptHistory(String key, String owner, String content) {
        ZSetOperations<String, String> cacheZSet = redisCache.getCacheZSet();
        if (cacheZSet.zCard(key) > 20) {
            //如果超出阈值，则删除最前面数据
            cacheZSet.remove(key, 0, 0);
        }
        cacheZSet.add(key, JSON.toJSONString(new XfChatRequest.Text(owner, content)), new Date().getTime());
    }
}