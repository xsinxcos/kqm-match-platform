package com.chaos.post.domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 帖子社群关系表(PostGroup)表实体类
 *
 * @author wzq
 * @since 2024-05-15 22:37:15
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_post_group")
public class PostGroup {
    //社群ID@TableId
    private Long groupId;
    //帖子ID@TableId
    private Long postId;


}

