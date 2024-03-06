package com.chaos.controller;

import com.chaos.annotation.AuthAdminCheck;
import com.chaos.domain.dto.AddCommentDto;
import com.chaos.domain.dto.DeleteCommentDto;
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
     *
     * @param addCommentDto
     * @return
     */
    @PostMapping("/add")
    public ResponseResult addComment(@RequestBody AddCommentDto addCommentDto) {
        return commentService.addComment(addCommentDto);
    }

    /**
     * 分页获取评论
     *
     * @param postId   帖子ID
     * @param pageNum  页码
     * @param pageSize 页条数
     * @return
     */
    @PostMapping("/list")
    public ResponseResult listCommentByPostId(Long postId, Integer pageNum, Integer pageSize) {
        return commentService.listCommentByPostId(postId, pageNum, pageSize);
    }

    /**
     * 删除评论
     *
     * @param dto DeleteCommentDto
     * @return
     */
    @PostMapping("/delete")
    public ResponseResult deleteComment(@RequestBody DeleteCommentDto dto) {
        return commentService.deleteComment(dto);
    }

    /**
     * 分页查询评论的子评论
     *
     * @param commentId 评论ID
     * @param pageSize  pageSize
     * @param pageNum   pageNum
     * @return
     */
    @PostMapping("/get")
    public ResponseResult showChildCommentById(Long commentId, Integer pageSize, Integer pageNum) {
        return commentService.showChildCommentById(commentId, pageSize, pageNum);
    }
}
