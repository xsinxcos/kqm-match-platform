package com.chaos.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.entity.Message;
import com.chaos.mapper.MessageMapper;
import com.chaos.service.MessageService;
import org.springframework.stereotype.Service;

/**
 * 消息表(Message)表服务实现类
 *
 * @author chaos
 * @since 2024-01-25 21:00:20
 */
@Service("messageService")

public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

}

