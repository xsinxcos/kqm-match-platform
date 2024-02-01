package com.chaos.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.entity.PostTag;
import com.chaos.mapper.PostTagMapper;
import com.chaos.service.PostTagService;
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

