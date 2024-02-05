package com.chaos.service.impl;

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

import java.util.List;

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
        queryWrapper.and(
                wrapper -> wrapper.eq("msg_from", userId).eq("msg_to", myUserID)
        ).or(
                wrapper -> wrapper.eq("msg_from", myUserID).eq("msg_to", userId)
        ).orderByDesc("send_time");

        Page<Message> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        List<HistoryMessageVo> vos = BeanCopyUtils.copyBeanList(page.getRecords(), HistoryMessageVo.class);
        return ResponseResult.okResult(new PageVo(vos, page.getTotal()));

    }
}

