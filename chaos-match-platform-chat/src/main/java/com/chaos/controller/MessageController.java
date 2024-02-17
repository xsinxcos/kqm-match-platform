package com.chaos.controller;

import com.chaos.domain.dto.ChatGPTMessageDto;
import com.chaos.domain.vo.ChatGPTMessageVo;
import com.chaos.response.ResponseResult;
import com.chaos.server.XfModelServerListener;
import com.chaos.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
    private final XfModelServerListener xfListener;

    /**
     * 展示用户之间的历史记录
     *
     * @return
     */
    @GetMapping("/history")
    public ResponseResult showHistoryMessage(Integer pageNum, Integer pageSize, Long userId) {
        return messageService.showHistoryMessage(pageNum, pageSize, userId);
    }

    /**
     * 与GPT对话
     *
     * @param question
     * @return
     */
    @PostMapping("/gpt")
    public ResponseResult chatWithGPT(@RequestBody ChatGPTMessageDto chatGPTMessageDto) {
        String question = chatGPTMessageDto.getQuestion();
        long timeOut = 30;
        String answer = "";
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                XfModelServerListener websocket = xfListener.sendQuestion(question, xfListener);
                while (!xfListener.is_finished) {
                    continue;
                }
                return websocket.getTotalAnswer();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        try {
            answer = future.get(timeOut, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException("回答失败");
        }

        return ResponseResult.okResult(new ChatGPTMessageVo(answer));
    }
}
