package com.chaos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.config.vo.PageVo;
import com.chaos.domain.entity.Message;
import com.chaos.domain.vo.HistoryChatUserVo;
import com.chaos.domain.vo.HistoryMessageVo;
import com.chaos.feign.UserFeignClient;
import com.chaos.feign.bo.PosterBo;
import com.chaos.mapper.MessageMapper;
import com.chaos.response.ResponseResult;
import com.chaos.service.MessageService;
import com.chaos.util.BeanCopyUtils;
import com.chaos.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 消息表(Message)表服务实现类
 *
 * @author chaos
 * @since 2024-01-25 21:00:20
 */
@Service("messageService")
@RequiredArgsConstructor
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    private final UserFeignClient userFeignClient;

    @Override
    public ResponseResult showHistoryMessage(Integer offset, Integer limit, Long userId) {
        Long myUserID = SecurityUtils.getUserId();
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();

        //分页获取离线消息
        queryWrapper.and(
                wrapper -> wrapper.eq("msg_from", userId).eq("msg_to", myUserID)
        ).or(
                wrapper -> wrapper.eq("msg_from", myUserID).eq("msg_to", userId)
        ).orderByDesc("send_time");

        long total = list(queryWrapper).size();

        queryWrapper.last("limit " + limit + " offset " + offset);

        List<Message> messages = list(queryWrapper);
        //包装Vo
        List<HistoryMessageVo> vos = BeanCopyUtils.copyBeanList(messages, HistoryMessageVo.class);
        return ResponseResult.okResult(new PageVo(vos, total));

    }

    @Override
    public ResponseResult showHistoryChatUser() {
        //获取用户userid
        Long userId = SecurityUtils.getUserId();
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Message::getMsgFrom, userId)
                .or()
                .eq(Message::getMsgTo, userId);
        //筛选出对应历史Id
        List<Long> ids = list(wrapper).stream()
                .flatMap(o -> Stream.of(o.getMsgTo(), o.getMsgFrom()))
                .distinct()
                .filter(id -> !id.equals(userId))
                .collect(Collectors.toList());

        List<HistoryChatUserVo> vos = new ArrayList<>();

        if(ids.isEmpty()) return ResponseResult.okResult(vos);

        Map<Long, PosterBo> posterBoMap = userFeignClient.getBatchUserByUserIds(ids).getData();



        for(Map.Entry<Long ,PosterBo> entry : posterBoMap.entrySet()){
            HistoryChatUserVo chatUserVo = HistoryChatUserVo.builder()
                    .id(entry.getKey())
                    .avatar(entry.getValue().getAvatar())
                    .userName(entry.getValue().getUserName())
                    .build();

            vos.add(chatUserVo);
        }


        return ResponseResult.okResult(vos);
    }
}

