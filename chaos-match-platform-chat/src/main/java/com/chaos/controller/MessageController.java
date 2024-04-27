package com.chaos.controller;

import com.chaos.annotation.SystemLog;
import com.chaos.domain.dto.ChatGPTMessageDto;
import com.chaos.domain.vo.ChatGPTMessageVo;
import com.chaos.response.ResponseResult;
import com.chaos.spark.SparkModel;
import com.chaos.spark.listener.SparkModelServerListener;
import com.chaos.service.MessageService;
import com.chaos.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @description: MessageController
 * @author: xsinxcos
 * @create: 2024-01-26 03:00
 **/

/**
 * 聊天模块（APP端）
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/chat")
public class MessageController {
    private final MessageService messageService;

    private final SparkModel sparkModel;
    /**
     * 展示用户之间的历史记录
     *
     * @return
     */
    @GetMapping("/history")
    @SystemLog(BusinessName = "showHistoryMessage")
    public ResponseResult showHistoryMessage(@NotNull Integer offset, @NotNull Integer limit, @NotNull Long userId) {
        return messageService.showHistoryMessage(offset, limit, userId);
    }


    /**
     * 获取用户历史聊天对象
     *
     * @return
     */
    @GetMapping("/history/user")
    @SystemLog(BusinessName = "showHistoryChatUser")
    public ResponseResult showHistoryChatUser() {
        return messageService.showHistoryChatUser();
    }

    /**
     * 与GPT对话
     * @return
     */
    @PostMapping("/gpt")
    @SystemLog(BusinessName = "chatWithGPT")
    public ResponseResult chatWithGPT(@RequestBody @Valid ChatGPTMessageDto chatGPTMessageDto) {
        String question = chatGPTMessageDto.getQuestion();
        Long userId = SecurityUtils.getUserId();
        sparkModel.sendMessage(userId,question);
        String answer = sparkModel.getAnswer(userId);

        return ResponseResult.okResult(new ChatGPTMessageVo(answer));
    }
}
