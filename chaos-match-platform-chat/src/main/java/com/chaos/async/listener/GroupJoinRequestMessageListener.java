package com.chaos.async.listener;

import com.alibaba.fastjson.JSON;
import com.chaos.async.event.GroupJoinRequestMessageEvent;
import com.chaos.domain.bo.GroupJoinBo;
import com.chaos.domain.bo.MatchBo;
import com.chaos.domain.bo.MessageBo;
import com.chaos.template.RedisKeyTemplate;
import com.chaos.util.RedisCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-05-12 20:08
 **/
@Component
@Slf4j
@RequiredArgsConstructor
public class GroupJoinRequestMessageListener implements ApplicationListener<GroupJoinRequestMessageEvent> {
    private final RedisCache redisCache;

    private final int GROUP_JOIN_EXPIRED_TIME = 60 * 60 * 24 * 3;

    @Async("asyncExecutor")
    @Override
    public void onApplicationEvent(GroupJoinRequestMessageEvent event) {
        MessageBo matchMessage = event.getGroupJoinRequestMessage();
        //获取三个关键参数 匹配发起者、被邀请者、帖子ID、社群ID
        long groupJoinFrom = matchMessage.getMessage().getSendFrom();
        long groupJoinTo = matchMessage.getMessage().getSendTo();
        long postId = matchMessage.getMessage().getPostId();
        long groupId = matchMessage.getMessage().getGroupId();

        //加入BO
        GroupJoinBo groupJoinBo = new GroupJoinBo(groupJoinFrom ,groupJoinTo ,postId ,groupId);
        //加入缓存
        String key = RedisKeyTemplate.GroupJoinKey(groupJoinFrom ,groupJoinTo, postId, groupId);

        redisCache.setCacheObject(key, JSON.toJSONString(groupJoinBo), GROUP_JOIN_EXPIRED_TIME, TimeUnit.SECONDS);
    }
}
