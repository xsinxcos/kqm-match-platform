package com.chaos.spark.listener;

import com.alibaba.fastjson.JSON;
import com.chaos.spark.entity.SparkText;
import com.chaos.spark.entity.SparkChatRequest;
import com.chaos.spark.entity.SparkChatResponse;
import com.chaos.utils.XfGPTUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
public class SparkModelServerListener extends WebSocketListener {

    private String totalAnswer;

    public boolean isFinished;

    private WebSocket nowWebSocket;

    public SparkModelServerListener() {
        totalAnswer = "";
        isFinished = false;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        // System.out.println(userId + "用来区分那个用户的结果" + text);
        SparkChatResponse myJsonParse = JSON.parseObject(text, SparkChatResponse.class);
        if (myJsonParse.getHeader().getCode() != 0) {
            log.error("发生错误，错误码为：" + myJsonParse.getHeader().getCode());
            log.error("本次请求的sid为：" + myJsonParse.getHeader().getSid());
            webSocket.close(1000, "");
        }
        List<SparkChatResponse.Text> textList = myJsonParse.getPayload().getChoices().getText();
        for (SparkChatResponse.Text temp : textList) {
            totalAnswer = totalAnswer + temp.getContent();
        }
        if (myJsonParse.getHeader().getStatus() == 2) {
            isFinished = true;
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
            log.info("大模型调用失败");
        }
    }

    public void sendQuestion(SparkModelServerListener webSocketListener ,List<SparkText> history) {
        //构建鉴权url
        String authUrl = null;
        try {
            authUrl = XfGPTUtils.getAuthUrl();
        } catch (Exception e) {
            throw new RuntimeException("authUrl构造失败");
        }
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        String url = authUrl.toString().replace("http://", "ws://").replace("https://", "wss://");
        //建立websocket请求
        Request request = new Request.Builder().url(url).build();
        WebSocket webSocket = client.newWebSocket(request, webSocketListener);
        nowWebSocket = webSocket;
        //构建请求内容
        SparkChatRequest request1 = XfGPTUtils.getRequest(refactorSparkMessages(history));
        //发送请求
        String message = JSON.toJSON(request1).toString();
        isFinished = false;
        webSocket.send(message);
    }

    public void onClosed() {
        nowWebSocket.close(1000, "");
    }

    private SparkChatRequest.Message refactorSparkMessages(List<SparkText> sparkTexts){
        SparkChatRequest.Message messages = new SparkChatRequest.Message();
        for (SparkText sparkText : sparkTexts) {
            SparkChatRequest.Text text = new SparkChatRequest.Text(sparkText.getRole(), sparkText.getContent());
            messages.getText().add(text);
        }

        return messages;
    }

}