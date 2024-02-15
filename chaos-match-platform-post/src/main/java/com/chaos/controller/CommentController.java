package com.chaos.controller;

import com.chaos.domain.dto.AddCommentDto;
import com.chaos.response.ResponseResult;
import com.chaos.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: CommentController
 * @author: xsinxcos
 * @create: 2024-02-02 05:24
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;
    /**
     * 添加评论
     * @param addCommentDto
     * @return
     */
    @PostMapping
    public ResponseResult addComment(@RequestBody AddCommentDto addCommentDto){
        return commentService.addComment(addCommentDto);
    }
}
