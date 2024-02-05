package com.chaos.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaos.domain.dto.AddPostDto;
import com.chaos.domain.dto.ModifyMyPostDto;
import com.chaos.domain.entity.Post;
import com.chaos.response.ResponseResult;


/**
 * 帖子表(Post)表服务接口
 *
 * @author chaos
 * @since 2024-02-01 07:56:42
 */
public interface PostService extends IService<Post> {

    ResponseResult addPost(AddPostDto addPostDto);

    ResponseResult listPost(Integer pageNum, Integer pageSize, Long tagId);

    ResponseResult showPost(Long id);

    ResponseResult getMyPost(Integer pageNum, Integer pageSize);

    ResponseResult modifyMyPost(ModifyMyPostDto modifyMyPostDto);
}

