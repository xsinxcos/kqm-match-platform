package com.chaos.post.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.post.domain.entity.PostTag;
import com.chaos.post.mapper.PostTagMapper;
import com.chaos.post.service.PostTagService;
import org.springframework.stereotype.Service;

/**
 * 帖子标签关系表(PostTag)表服务实现类
 *
 * @author chaos
 * @since 2024-02-01 08:25:45
 */
@Service("postTagService")
public class PostTagServiceImpl extends ServiceImpl<PostTagMapper, PostTag> implements PostTagService {

}

