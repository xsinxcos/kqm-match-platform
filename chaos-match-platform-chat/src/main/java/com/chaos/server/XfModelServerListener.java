package com.chaos.server;

import com.alibaba.fastjson.JSON;
import com.chaos.domain.XfChatRequest;
import com.chaos.domain.XfChatResponse;
import com.chaos.utils.XfGPTUtils;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
@Getter
public class XfModelServerListener extends WebSocketListener {

    private String totalAnswer;

    public boolean is_finished;

    public XfModelServerListener(){
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

    public XfModelServerListener sendQuestion(String question ,XfModelServerListener webSocketListener) throws Exception {
        //构建鉴权url
        String authUrl = XfGPTUtils.getAuthUrl();
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        String url = authUrl.toString().replace("http://", "ws://").replace("https://", "wss://");
        //建立websocket请求
        Request request = new Request.Builder().url(url).build();
        WebSocket webSocket = client.newWebSocket(request ,webSocketListener);
        //构建请求内容
        List<XfChatRequest.Text> requestTexts = new ArrayList<>();
        requestTexts.add(new XfChatRequest.Text("user" ,question));
        XfChatRequest request1 = XfGPTUtils.getRequest(requestTexts);
        //发送请求
        String message = JSON.toJSON(request1).toString();
        is_finished = false;
        webSocket.send(message);

        return webSocketListener;
    }

}