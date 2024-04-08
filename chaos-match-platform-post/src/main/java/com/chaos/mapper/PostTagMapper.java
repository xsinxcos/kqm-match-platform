package com.chaos.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaos.domain.entity.PostTag;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


/**
 * 帖子标签关系表(PostTag)表数据库访问层
 *
 * @author chaos
 * @since 2024-02-01 08:25:45
 */
public interface PostTagMapper extends BaseMapper<PostTag> {
    @Select("SELECT tag_id, COUNT(post_id) as post_count FROM t_post_tag GROUP BY tag_id")
    List<Map<String, Long>> selectTagIdAndPostCount();

    List<PostTag> getPostTagsByPostIds(List<Long> postIds);
}
