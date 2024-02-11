package com.chaos.domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 帖子与用户匹配成功关系表(PostUser)表实体类
 *
 * @author chaos
 * @since 2024-02-04 03:55:29
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_post_user")
public class PostUser {
    //帖子ID@TableId
    private Long postId;
    //用户ID@TableId
    private Long userId;
    //0为帖子与用户匹配关系 1为帖子与用户收藏关系
    private Integer status;
}

