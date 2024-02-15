package com.chaos.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.config.vo.PageVo;
import com.chaos.constant.AppHttpCodeEnum;
import com.chaos.domain.dto.AddCommentDto;
import com.chaos.domain.entity.Comment;
import com.chaos.domain.vo.CommentVo;
import com.chaos.exception.SystemException;
import com.chaos.feign.UserFeignClient;
import com.chaos.feign.bo.PosterBo;
import com.chaos.mapper.CommentMapper;
import com.chaos.response.ResponseResult;
import com.chaos.service.CommentService;
import com.chaos.util.BeanCopyUtils;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author chaos
 * @since 2024-02-01 07:59:28
 */
@Service("commentService")
@RequiredArgsConstructor
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    //帖子评论的type
    private final static Integer COMMENT_TYPE_POST = 0;
    //对评论进行总展示时，每条评论子评论的条数
    private final static Integer CHILD_COMMENT_COUNT = 3;
    //根评论ID标识
    private final static Integer DEFAULT_ROOT_ID = -1;
    private final UserFeignClient userFeignClient;
    @Override
    public ResponseResult addComment(AddCommentDto addCommentDto) {
        if(!StringUtils.hasText(addCommentDto.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        Comment comment = BeanCopyUtils.copyBean(addCommentDto, Comment.class);
        save(comment);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listCommentByPostId(Long postId, Integer pageNum, Integer pageSize) {
        //获取符合条件的评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getType, COMMENT_TYPE_POST)
                .eq(Comment::getPostId ,postId)
                .eq(Comment::getRootId ,DEFAULT_ROOT_ID)
                .orderByAsc(Comment::getCreateTime);
        //分页获取
        Page<Comment> page = new Page<>(pageNum ,pageSize);
        page(page ,queryWrapper);
        //获取评论发表者的基本信息
        List<Long> ids = page.getRecords().stream()
                .map(Comment::getCreateBy)
                .collect(Collectors.toList());
        //feign调用获取用户信息
        Map<Long, PosterBo> map = userFeignClient.getBatchUserByUserIds(ids).getData();
        //封装进VO
        List<CommentVo> commentVos = new ArrayList<>();
        for (Comment record : page.getRecords()) {
            PosterBo user = map.get(record.getCreateBy());
            CommentVo commentVo = BeanCopyUtils.copyBean(record, CommentVo.class);
            commentVo.setAvatar(user.getAvatar());
            commentVo.setUsername(user.getUsername());
            commentVo.setChildren(getChildCommentForList(record.getId()));
            commentVos.add(commentVo);
        }
        return ResponseResult.okResult(new PageVo(commentVos ,page.getTotal()));
    }

    private List<CommentVo> getChildCommentForList(Long rootId){
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        //找出三条最早的子评论
        wrapper.eq(Objects.nonNull(rootId) ,"root_id" ,rootId)
                .orderByAsc("create_time")
                .last("limit " + CHILD_COMMENT_COUNT);
        //获取对应的用户信息
        List<Comment> list = list(wrapper);
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        List<Long> ids = commentVos.stream()
                .map(CommentVo::getCreateBy)
                .collect(Collectors.toList());
        //feign调用获取用户信息
        Map<Long, PosterBo> map = userFeignClient.getBatchUserByUserIds(ids).getData();
        //将用户信息封装进VO
        for (CommentVo commentVo : commentVos) {
            Long createBy = commentVo.getCreateBy();
            commentVo.setUsername(map.get(createBy).getUsername());
            commentVo.setAvatar(map.get(createBy).getAvatar());
        }
        return commentVos;
    }
}

