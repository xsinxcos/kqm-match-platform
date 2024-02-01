package com.chaos.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 评论表(Comment)表实体类
 *
 * @author chaos
 * @since 2024-02-01 07:59:28
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_comment")
public class Comment {
    @TableId
    private Long id;

    //评论类型（0代表帖子）
    private Integer type;
    //帖子id
    private Long postId;
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
    //更新者
    private Long updateBy;
    //更新时间
    private Date updateTime;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;


}

