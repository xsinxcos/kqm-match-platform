package com.chaos.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.entity.MessageSend;
import com.chaos.mapper.MessageSendMapper;
import com.chaos.service.MessageSendService;
import org.springframework.stereotype.Service;

/**
 * 发送消息表(MessageSend)表服务实现类
 *
 * @author chaos
 * @since 2024-01-23 05:12:45
 */
@Service("messageSendService")
public class MessageSendServiceImpl extends ServiceImpl<MessageSendMapper, MessageSend> implements MessageSendService {

}

