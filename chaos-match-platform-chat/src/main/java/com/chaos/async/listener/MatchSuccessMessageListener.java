package com.chaos.async.listener;

import com.chaos.async.event.MatchSuccessMessageEvent;
import com.chaos.domain.bo.MessageBo;
import com.chaos.feign.PostFeignClient;
import com.chaos.feign.bo.AddTeamUserMatchRelationBo;
import com.chaos.template.RedisKeyTemplate;
import com.chaos.util.RedisCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 匹配成功消息处理类
 * @author: xsinxcos
 * @create: 2024-02-04 01:19
 **/
@Component
@RequiredArgsConstructor
@Slf4j
public class MatchSuccessMessageListener implements ApplicationListener<MatchSuccessMessageEvent> {
    private final RedisCache redisCache;
    private final PostFeignClient postFeignClient;

    @Async("asyncExecutor")
    @Override
    public void onApplicationEvent(MatchSuccessMessageEvent event) {
        MessageBo matchResultMessage = event.getMatchResultMessage();
        //获取三个关键参数 匹配发起者、被邀请者、帖子ID
        Long matchFrom = matchResultMessage.getMessage().getSendTo();
        Long matchTo = matchResultMessage.getMessage().getSendFrom();
        Long matchPost = matchResultMessage.getMessage().getPostId();
        //匹配成功，将Redis中的数据删除
        String key = RedisKeyTemplate.matchKey(matchFrom, matchTo, matchPost);

        if (!redisCache.isExist(key)) {
            throw new RuntimeException("匹配过期或者不存在此匹配");
        }
        //进行feign调用持久化消息（帖子与用户匹配关系表）
        List<Long> userIDs = new ArrayList<>();

        userIDs.add(matchFrom);
        userIDs.add(matchTo);

        postFeignClient.addTeamUserMatchRelation(
                new AddTeamUserMatchRelationBo(
                        matchPost,
                        userIDs
                )
        );
        log.info("帖子与用户匹配关系持久化成功");
        //删除redis中的数据
        redisCache.deleteObject(key);
    }
}
