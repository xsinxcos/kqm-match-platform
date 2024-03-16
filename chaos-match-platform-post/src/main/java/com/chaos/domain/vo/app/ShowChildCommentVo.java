package com.chaos.domain.vo.app;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @description: ShowCommentVo
 * @author: xsinxcos
 * @create: 2024-02-16 04:53
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShowChildCommentVo {
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
    //所回复的目标评论的username
    private String toCommentUserName;
    //所回复评论id
    private Long toCommentId;
    //创建者
    private Long createBy;
    //创建时间
    private Date createTime;
}
