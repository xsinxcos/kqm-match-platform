package com.chaos.async.listener;

import com.chaos.async.event.GroupJoinFailMessageEvent;
import com.chaos.domain.bo.MessageBo;
import com.chaos.template.RedisKeyTemplate;
import com.chaos.util.RedisCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-05-12 20:57
 **/
@Component
@Slf4j
@RequiredArgsConstructor
public class GroupJoinFailMessageListener implements ApplicationListener<GroupJoinFailMessageEvent> {
    private final RedisCache redisCache;

    @Async("asyncExecutor")
    @Override
    public void onApplicationEvent(GroupJoinFailMessageEvent event) {
        MessageBo failMessage = event.getGroupJoinFailMessage();
        //获取三个关键参数 匹配发起者、被邀请者、帖子ID、社群ID
        long groupJoinFrom = failMessage.getMessage().getSendTo();
        long groupJoinTo = failMessage.getMessage().getSendFrom();
        long postId = failMessage.getMessage().getPostId();
        long groupId = failMessage.getMessage().getGroupId();
        //将Redis中的数据删除
        String key = RedisKeyTemplate.GroupJoinKey(groupJoinFrom, groupJoinTo, postId, groupId);
        //删除redis中的数据
        redisCache.deleteObject(key);
    }

}
