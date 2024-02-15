package com.chaos.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-02-16 00:32
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCommentDto {
    //帖子ID
    private Long postId;
    //评论类型
    private Integer type;
    //根评论ID
    private Long rootId;
    //所回复评论id
    private Long toCommentId;
    //所回复的目标评论的userid
    private Long toCommentUserId;
    //评论内容
    private String content;
}
