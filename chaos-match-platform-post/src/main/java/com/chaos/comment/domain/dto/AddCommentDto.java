package com.chaos.comment.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
    @NotNull(message = "postId不能null")
    private Long postId;
    //评论类型
    @NotNull(message = "评论类型不能为null")
    private Integer type;
    //根评论ID
    @NotNull(message = "rootId不能null")
    private Long rootId;
    //所回复评论id
    private Long toCommentId;
    //所回复的目标评论的userid
    private Long toCommentUserId;
    //评论内容
    @NotBlank(message = "评论内容不能为空")
    private String content;
}
