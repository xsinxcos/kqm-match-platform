package com.chaos.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.entity.PostUser;
import com.chaos.mapper.PostUserMapper;
import com.chaos.service.PostUserService;
import org.springframework.stereotype.Service;

/**
 * 帖子与用户匹配成功关系表(PostUser)表服务实现类
 *
 * @author chaos
 * @since 2024-02-04 03:55:30
 */
@Service("postUserService")
public class PostUserServiceImpl extends ServiceImpl<PostUserMapper, PostUser> implements PostUserService {

}

