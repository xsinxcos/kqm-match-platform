package com.chaos.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.entity.Post;
import com.chaos.mapper.PostMapper;
import com.chaos.service.PostService;
import org.springframework.stereotype.Service;

/**
 * 帖子表(Post)表服务实现类
 *
 * @author chaos
 * @since 2024-02-01 07:56:43
 */
@Service("postService")
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

}

