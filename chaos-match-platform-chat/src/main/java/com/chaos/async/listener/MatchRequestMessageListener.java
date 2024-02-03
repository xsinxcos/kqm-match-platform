package com.chaos.async.listener;

import com.alibaba.fastjson.JSON;
import com.chaos.async.event.MatchRequestMessageEvent;
import com.chaos.domain.bo.MatchBo;
import com.chaos.domain.bo.MessageBo;
import com.chaos.util.RedisCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @description: 匹配请求消息监听类
 * @author: xsinxcos
 * @create: 2024-02-04 01:17
 **/
@Component
@RequiredArgsConstructor
public class MatchRequestMessageListener implements ApplicationListener<MatchRequestMessageEvent> {
    private final RedisCache redisCache;
    //匹配消息有效
    private final Integer MATCH_EXPIRED_TIME = 60 * 60 * 24 * 3;
    @Async
    @Override
    public void onApplicationEvent(MatchRequestMessageEvent event) {
        MessageBo matchMessage = event.getMatchMessage();
        //获取三个关键参数 匹配发起者、被邀请者、帖子ID
        Long matchFrom = matchMessage.getMessage().getSendFrom();
        Long matchTo = matchMessage.getMessage().getSendTo();
        Long matchPost = matchMessage.getMessage().getPostId();
        //加入BO
        MatchBo matchBo = new MatchBo(matchFrom ,matchTo ,matchPost);
        String key = "USER:" + matchFrom + " invited USER:" + matchTo + "with " + "POST:" + matchPost;
        redisCache.setCacheObject(key , JSON.toJSONString(matchBo) ,MATCH_EXPIRED_TIME , TimeUnit.SECONDS);
    }
}
