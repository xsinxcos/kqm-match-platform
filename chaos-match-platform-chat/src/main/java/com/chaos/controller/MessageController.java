package com.chaos.controller;

import com.chaos.response.ResponseResult;
import com.chaos.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: MessageController
 * @author: xsinxcos
 * @create: 2024-01-26 03:00
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class MessageController {
    private final MessageService messageService;

    /**
     * 展示用户之间的历史记录
     * @return
     */
    @GetMapping("/history")
    public ResponseResult showHistoryMessage(Integer pageNum ,Integer pageSize ,Long userId){
        return messageService.showHistoryMessage(pageNum ,pageSize ,userId);
    }
}
