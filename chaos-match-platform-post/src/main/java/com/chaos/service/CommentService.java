package com.chaos.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaos.domain.dto.admin.AdminDeleteCommentDto;
import com.chaos.domain.dto.app.AddCommentDto;
import com.chaos.domain.dto.app.DeleteCommentDto;
import com.chaos.domain.entity.Comment;
import com.chaos.response.ResponseResult;


/**
 * 评论表(Comment)表服务接口
 *
 * @author chaos
 * @since 2024-02-01 07:59:28
 */
public interface CommentService extends IService<Comment> {

    ResponseResult addComment(AddCommentDto addCommentDto);

    ResponseResult listCommentByPostId(Long postId, Integer pageNum, Integer pageSize);

    ResponseResult deleteComment(DeleteCommentDto dto);

    ResponseResult showChildCommentById(Long commentId, Integer pageSize, Integer pageNum);

    ResponseResult adminDeleteComment(AdminDeleteCommentDto dto);
}

