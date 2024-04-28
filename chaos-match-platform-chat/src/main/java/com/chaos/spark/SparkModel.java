package com.chaos.spark;

import com.chaos.async.AsyncThreadPoolConfig;
import com.chaos.spark.entity.SparkText;
import com.chaos.spark.entity.SparkHistoryMessage;
import com.chaos.spark.listener.SparkModelServerListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.*;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-04-27 23:34
 **/
@Component
@RequiredArgsConstructor
public class SparkModel implements ISparkModel {
    //用于并发情况下每个用户ws连接不冲突
    private static final ConcurrentHashMap<Long , SparkModelServerListener> sparkListeners = new ConcurrentHashMap<>();

    //存储每个用户的历史记录
    private static final ConcurrentHashMap<Long , SparkHistoryMessage> historyMessages = new ConcurrentHashMap<>();

    //历史记录保留时间
    private static final Long timeOut = 60L;

    private static final TimeUnit timeUnit = TimeUnit.MINUTES;

    //配置CompletableFuture线程池
    private final AsyncThreadPoolConfig threadPoolConfig;


    @Override
    public void sendMessage(Long id, String text) {
        SparkModelServerListener pastListener ;
        if(sparkListeners.containsKey(id)){
            throw new RuntimeException("消息发送频繁，请稍等");
        }else {
            pastListener = new SparkModelServerListener();
            sparkListeners.put(id ,pastListener);
        }

        SparkModelServerListener curListener = new SparkModelServerListener();

        //获取历史记录，如果最后历史记录间隔大于某个间隔，直接抛弃
        SparkHistoryMessage historyMessage = historyMessages.get(id);

        if(isExpired(historyMessage)){
            historyMessage = new SparkHistoryMessage();
        }

        //将消息放入消息总列表
        SparkText sparkText = new SparkText("user" ,text);

        historyMessage.getMessages().add(sparkText);
        historyMessage.setLastTime(System.currentTimeMillis());

        historyMessages.put(id ,historyMessage);

        curListener.sendQuestion(curListener ,historyMessage.getMessages());

        sparkListeners.put(id ,curListener);
    }

    private boolean isExpired(SparkHistoryMessage messages){
        if(Objects.isNull(messages)) return true;
        Long lastTime = messages.getLastTime();
        long curTime = System.currentTimeMillis();
        return curTime - lastTime > timeUnit.toMillis(timeOut);
    }

    @Override
    public String getAnswer(Long id) {
        SparkModelServerListener listener = sparkListeners.get(id);
        String res = "";

        Long completableFutureTimeOut = 30L;
        //获取回答
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                while (!listener.isFinished) {
                    Thread.sleep(200);
                    continue;
                }
                return listener.getTotalAnswer();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                listener.onClosed();
            }
        } ,threadPoolConfig.asyncThreadPool());

        try {
            res = future.get(completableFutureTimeOut, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException("回答失败");
        }

        //加入历史记录
        SparkHistoryMessage historyMessage = historyMessages.get(id);
        historyMessage.getMessages().add(new SparkText("assistant" ,res));

        //将使用后的ws移除
        sparkListeners.remove(id);

        return res;
    }

}
