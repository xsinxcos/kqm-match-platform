package com.chaos.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.entity.MessageReceive;
import com.chaos.mapper.MessageReceiveMapper;
import com.chaos.service.MessageReceiveService;
import org.springframework.stereotype.Service;

/**
 * 推送消息表（保存用户收到的消息）(MessageReceive)表服务实现类
 *
 * @author chaos
 * @since 2024-01-23 05:13:43
 */
@Service("messageReceiveService")
public class MessageReceiveServiceImpl extends ServiceImpl<MessageReceiveMapper, MessageReceive> implements MessageReceiveService {

}

