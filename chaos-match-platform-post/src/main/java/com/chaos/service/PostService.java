package com.chaos.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaos.domain.dto.admin.AdminDeletePostDto;
import com.chaos.domain.dto.admin.AdminListPostDto;
import com.chaos.domain.dto.app.*;
import com.chaos.domain.entity.Post;
import com.chaos.feign.bo.AddPostUserMatchRelationBo;
import com.chaos.response.ResponseResult;


/**
 * 帖子表(Post)表服务接口
 *
 * @author chaos
 * @since 2024-02-01 07:56:42
 */
public interface PostService extends IService<Post> {


    ResponseResult addPost(AddPostDto addPostDto);

    ResponseResult listPost(ListPostDto listPostDto);

    ResponseResult showPost(Long id);

    ResponseResult getMyPost(Integer pageNum, Integer pageSize);

    ResponseResult modifyMyPost(ModifyMyPostDto modifyMyPostDto);

    ResponseResult deleteMyPost(Long id);

    ResponseResult addFavoritePost(AddFavoritePostDto addFavoritePostDto);

    ResponseResult deleteFavoritePost(DeleteFavoritePostDto dto);

    ResponseResult listFavoritePost(Integer pageNum, Integer pageSize);

    ResponseResult modifyPostStatus(ModifyPostStatusDto modifyPostStatusDto);

    ResponseResult addPostUserMatchRelation(AddPostUserMatchRelationBo addPostUserMatchRelationBo);

    ResponseResult getMatchRelationByPostId(Long postId);

    ResponseResult cancelMatchByPostId(Long postId);

    ResponseResult getMeMatchedPost(Integer pageNum, Integer pageSize);

    ResponseResult adminListPost(AdminListPostDto adminListPostDto);

    ResponseResult adminDeletePost(AdminDeletePostDto adminDeletePostDto);
}

