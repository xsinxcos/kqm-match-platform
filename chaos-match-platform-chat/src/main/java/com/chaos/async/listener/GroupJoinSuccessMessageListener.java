package com.chaos.async.listener;

import com.chaos.async.event.GroupJoinSuccessMessageEvent;
import com.chaos.domain.bo.MessageBo;
import com.chaos.feign.PostFeignClient;
import com.chaos.feign.bo.AddPostGroupRelationBo;
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
 * @create: 2024-05-12 21:18
 **/
@Component
@Slf4j
@RequiredArgsConstructor
public class GroupJoinSuccessMessageListener implements ApplicationListener<GroupJoinSuccessMessageEvent> {
    private final RedisCache redisCache;

    private PostFeignClient postFeignClient;

    @Async("asyncExecutor")
    @Override
    public void onApplicationEvent(GroupJoinSuccessMessageEvent event) {
        MessageBo successMessage = event.getGroupJoinSuccessMessage();
        //获取三个关键参数 匹配发起者、被邀请者、帖子ID、社群ID
        long groupJoinFrom = successMessage.getMessage().getSendTo();
        long groupJoinTo = successMessage.getMessage().getSendFrom();
        long postId = successMessage.getMessage().getPostId();
        long groupId = successMessage.getMessage().getGroupId();
        //匹配成功，将Redis中的数据删除
        String key = RedisKeyTemplate.GroupJoinKey(groupJoinFrom, groupJoinTo, postId, groupId);
        if (!redisCache.isExist(key)) {
            throw new RuntimeException("社群邀请过期或者不存在此社群邀请");
        }
        //进行feign调用持久化消息（帖子队伍与匹配关系表）
        postFeignClient.addPostGroupRelation(new AddPostGroupRelationBo(postId, groupId));

        log.info("社群与用户队伍匹配关系持久化成功");
        //删除redis中的数据
        redisCache.deleteObject(key);
    }
}
