package com.chaos.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @description: CommentVo
 * @author: xsinxcos
 * @create: 2024-02-16 01:58
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentVo {
    private Long id;

    //评论类型（0代表帖子）
    private Integer type;
    //帖子id
    private Long postId;
    //评论人用户名
    private String username;
    //评论人头像
    private String avatar;
    //评论内容
    private String content;
    //根评论id
    private Long rootId;
    //所回复的目标评论的userid
    private Long toCommentUserId;
    //所回复评论id
    private Long toCommentId;
    //创建者
    private Long createBy;
    //创建时间
    private Date createTime;
    //子评论
    private List<CommentVo> children;
}
