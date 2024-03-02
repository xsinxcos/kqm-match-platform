package com.chaos.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaos.domain.entity.Message;
import com.chaos.response.ResponseResult;


/**
 * 消息表(Message)表服务接口
 *
 * @author chaos
 * @since 2024-01-25 21:00:20
 */
public interface MessageService extends IService<Message> {

    ResponseResult showHistoryMessage(Integer pageNum, Integer pageSize, Long userId);

    ResponseResult showHistoryChatUser();
}

