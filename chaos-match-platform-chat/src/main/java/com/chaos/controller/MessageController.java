package com.chaos.controller;

import com.chaos.domain.dto.ChatGPTMessageDto;
import com.chaos.domain.vo.ChatGPTMessageVo;
import com.chaos.response.ResponseResult;
import com.chaos.server.XfModelServerListener;
import com.chaos.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RequestMapping("/chat")
public class MessageController {
    private final MessageService messageService;

    /**
     * 展示用户之间的历史记录
     *
     * @return
     */
    @GetMapping("/history")
    public ResponseResult showHistoryMessage(Integer offset, Integer limit, Long userId) {
        return messageService.showHistoryMessage(offset, limit, userId);
    }


    /**
     * 获取用户历史聊天对象
     *
     * @return
     */
    @GetMapping("/history/user")
    public ResponseResult showHistoryChatUser() {
        return messageService.showHistoryChatUser();
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
        XfModelServerListener xfListener = new XfModelServerListener();
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                XfModelServerListener websocket = xfListener.sendQuestion(question, xfListener);
                while (!xfListener.is_finished) {
                    Thread.sleep(200);
                    continue;
                }
                return websocket.getTotalAnswer();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                xfListener.onClosed();
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
