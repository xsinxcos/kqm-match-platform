package com.chaos.controller.app;

import com.chaos.annotation.SystemLog;
import com.chaos.domain.dto.app.AddCommentDto;
import com.chaos.domain.dto.app.DeleteCommentDto;
import com.chaos.response.ResponseResult;
import com.chaos.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
    @SystemLog(BusinessName = "addComment")
    public ResponseResult addComment(@RequestBody @Valid AddCommentDto addCommentDto) {
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
    @SystemLog(BusinessName = "listCommentByPostId")
    public ResponseResult listCommentByPostId(@NotNull Long postId, @NotNull Integer pageNum, @NotNull Integer pageSize) {
        return commentService.listCommentByPostId(postId, pageNum, pageSize);
    }

    /**
     * 删除评论
     *
     * @param dto DeleteCommentDto
     * @return
     */
    @PostMapping("/delete")
    @SystemLog(BusinessName = "deleteComment")
    public ResponseResult deleteComment(@RequestBody @Valid DeleteCommentDto dto) {
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
    @SystemLog(BusinessName = "showChildCommentById")
    public ResponseResult showChildCommentById(@NotNull Long commentId, @NotNull Integer pageSize, @NotNull Integer pageNum) {
        return commentService.showChildCommentById(commentId, pageSize, pageNum);
    }
}
