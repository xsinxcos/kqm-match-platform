package com.chaos.async.listener;

import com.chaos.async.event.MatchFailMessageEvent;
import com.chaos.domain.bo.MessageBo;
import com.chaos.template.RedisKeyTemplate;
import com.chaos.util.RedisCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @description: 匹配失败消息处理监听类
 * @author: xsinxcos
 * @create: 2024-02-04 01:14
 **/
@Component
@Slf4j
@RequiredArgsConstructor
public class MatchFailMessageListener implements ApplicationListener<MatchFailMessageEvent> {
    private final RedisCache redisCache;

    @Async("asyncExecutor")
    @Override
    public void onApplicationEvent(MatchFailMessageEvent event) {
        MessageBo matchResultMessage = event.getMatchResultMessage();
        //获取三个关键参数 匹配发起者、被邀请者、帖子ID
        Long matchFrom = matchResultMessage.getMessage().getSendTo();
        Long matchTo = matchResultMessage.getMessage().getSendFrom();
        Long matchPost = matchResultMessage.getMessage().getPostId();
        //匹配成功，将Redis中的数据删除
        String key = RedisKeyTemplate.matchKey(matchFrom ,matchTo ,matchPost);
        //删除redis中的数据
        redisCache.deleteObject(key);
    }
}
