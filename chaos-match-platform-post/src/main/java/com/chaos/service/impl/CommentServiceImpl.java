package com.chaos.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.constant.AppHttpCodeEnum;
import com.chaos.domain.dto.AddCommentDto;
import com.chaos.domain.entity.Comment;
import com.chaos.exception.SystemException;
import com.chaos.mapper.CommentMapper;
import com.chaos.response.ResponseResult;
import com.chaos.service.CommentService;
import com.chaos.util.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author chaos
 * @since 2024-02-01 07:59:28
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Override
    public ResponseResult addComment(AddCommentDto addCommentDto) {
        if(!StringUtils.hasText(addCommentDto.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        Comment comment = BeanCopyUtils.copyBean(addCommentDto, Comment.class);
        save(comment);
        return ResponseResult.okResult();
    }
}

