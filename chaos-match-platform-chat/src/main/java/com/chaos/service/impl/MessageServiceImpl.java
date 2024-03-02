package com.chaos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.config.vo.PageVo;
import com.chaos.domain.entity.Message;
import com.chaos.domain.vo.HistoryMessageVo;
import com.chaos.mapper.MessageMapper;
import com.chaos.response.ResponseResult;
import com.chaos.service.MessageService;
import com.chaos.util.BeanCopyUtils;
import com.chaos.util.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 消息表(Message)表服务实现类
 *
 * @author chaos
 * @since 2024-01-25 21:00:20
 */
@Service("messageService")

public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Override
    public ResponseResult showHistoryMessage(Integer pageNum, Integer pageSize, Long userId) {
        Long myUserID = SecurityUtils.getUserId();
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();

        //分页获取离线消息
        queryWrapper.and(
                wrapper -> wrapper.eq("msg_from", userId).eq("msg_to", myUserID)
        ).or(
                wrapper -> wrapper.eq("msg_from", myUserID).eq("msg_to", userId)
        ).orderByDesc("send_time");

        Page<Message> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        //包装Vo
        List<HistoryMessageVo> vos = BeanCopyUtils.copyBeanList(page.getRecords(), HistoryMessageVo.class);
        return ResponseResult.okResult(new PageVo(vos, page.getTotal()));

    }

    @Override
    public ResponseResult showHistoryChatUser() {
        //获取用户userid
        Long userId = SecurityUtils.getUserId();
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Message::getMsgFrom ,userId)
                .or()
                .eq(Message::getMsgTo ,userId);
        //筛选出对应历史Id
        List<Long> ids = list(wrapper).stream()
                .flatMap(o -> Stream.of(o.getMsgTo(), o.getMsgFrom()))
                .distinct()
                .filter(id -> !id.equals(userId))
                .collect(Collectors.toList());

        //包装vo返回
        Map<String ,List<Long>> idsMap = new HashMap<>();
        idsMap.put("userIds" ,ids);

        return ResponseResult.okResult(idsMap);
    }
}

