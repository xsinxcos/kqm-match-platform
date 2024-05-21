package com.chaos.post.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.post.domain.entity.PostGroup;
import com.chaos.post.mapper.PostGroupMapper;
import org.springframework.stereotype.Service;
import com.chaos.post.service.PostGroupService;

/**
 * 帖子社群关系表(PostGroup)表服务实现类
 *
 * @author wzq
 * @since 2024-05-15 22:37:16
 */
@Service("postGroupService")
public class PostGroupServiceImpl extends ServiceImpl<PostGroupMapper, PostGroup> implements PostGroupService {

}

