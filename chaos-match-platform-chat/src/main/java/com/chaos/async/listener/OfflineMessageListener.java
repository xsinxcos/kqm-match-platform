package com.chaos.async.listener;

import com.alibaba.fastjson.JSON;
import com.chaos.async.event.OfflineMessageEvent;
import com.chaos.constants.MessageConstants;
import com.chaos.domain.bo.MessageBo;
import com.chaos.util.RedisCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @description: 离线消息监听
 * @author: xsinxcos
 * @create: 2024-01-25 21:12
 **/

@Component
@Slf4j
@RequiredArgsConstructor
public class OfflineMessageListener implements ApplicationListener<OfflineMessageEvent> {


    private final RedisCache redisCache;


    /**
     * 将离线消息存储进redis缓存，每个用户缓存1000条数据，超出删除最早的消息
     * （PS：离线消息的作用：降低首次登陆的消息数据全量拉取）
     *
     * @param event 离线消息
     */
    @Async("asyncExecutor")
    @Override
    public void onApplicationEvent(OfflineMessageEvent event) {
        //获取离线消息的内容
        MessageBo messageBo = event.getOfflineMessage();
        Long sendTo = messageBo.getMessage().getSendTo();
        String userKey = MessageConstants.OFFLINE_MESSAGE_REDIS_KEY + sendTo;
        ZSetOperations<String, String> operations = redisCache.getCacheZSet();
        if (operations.zCard(userKey) > MessageConstants.USER_MAX_NUMBER_MESSAGE) {
            //如果队列数据超过阈值，则删除最前面数据
            operations.removeRange(userKey, 0, 0);
        }
        //插入数据，将uuid作为分值
        operations.add(userKey, JSON.toJSONString(messageBo), messageBo.getMessage().getUuid());
    }
}
