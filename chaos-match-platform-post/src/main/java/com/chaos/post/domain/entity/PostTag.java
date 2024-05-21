package com.chaos.post.domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 帖子标签关系表(PostTag)表实体类
 *
 * @author chaos
 * @since 2024-02-01 08:25:45
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_post_tag")
public class PostTag {
    //帖子ID@TableId
    private Long postId;
    //标签ID@TableId
    private Long tagId;


}

