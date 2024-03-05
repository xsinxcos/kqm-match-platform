package com.chaos.controller;

import com.chaos.domain.dto.*;
import com.chaos.domain.entity.Post;
import com.chaos.response.ResponseResult;
import com.chaos.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @description: PostController
 * @author: xsinxcos
 * @create: 2024-02-02 05:23
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    /**
     * 发布帖子
     *
     * @param addPostDto
     * @return
     */
    @PostMapping("/edit")
    public ResponseResult addPost(@RequestBody AddPostDto addPostDto) {
        return postService.addPost(addPostDto);
    }


    /**
     * 罗列条件帖子
     *
     * @param listPostDto listPostDto
     * @return
     */
    @PostMapping("/list")
    public ResponseResult listPost(@RequestBody ListPostDto listPostDto) {
        return postService.listPost(listPostDto);
    }

    /**
     * 展示帖子
     *
     * @param id
     * @return
     */
    @GetMapping("/list/{id}")
    public ResponseResult showPost(@PathVariable Long id) {
        return postService.showPost(id);
    }

    /**
     * 罗列本人帖子
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/getme")
    public ResponseResult getMyPost(Integer pageNum, Integer pageSize) {
        return postService.getMyPost(pageNum, pageSize);
    }

    /**
     * 修改本人帖子
     *
     * @param modifyMyPostDto
     * @return
     */
    @PutMapping("/getme")
    public ResponseResult modifyMyPost(@RequestBody ModifyMyPostDto modifyMyPostDto) {
        return postService.modifyMyPost(modifyMyPostDto);
    }

    /**
     * 删除本人帖子
     *
     * @param id 帖子ID
     * @return
     */
    @DeleteMapping("/getme/{id}")
    public ResponseResult deleteMyPost(@PathVariable String id) {
        return postService.deleteMyPost(id);
    }

    /**
     * 收藏帖子
     *
     * @param addFavoritePostDto
     * @return
     */
    @PostMapping("/favorite")
    public ResponseResult addFavoritePost(@RequestBody AddFavoritePostDto addFavoritePostDto) {
        return postService.addFavoritePost(addFavoritePostDto);
    }

    /**
     * 移除收藏的帖子
     *
     * @param dto
     * @return
     */

    @DeleteMapping("/favorite")
    public ResponseResult deleteFavoritePost(@RequestBody DeleteFavoritePostDto dto) {
        Post byId = postService.getById(dto.getId());
        Optional.ofNullable(byId).orElseThrow(() -> (new RuntimeException("操作失败")));
        return postService.deleteFavoritePost(dto);
    }

    /**
     * 罗列收藏的帖子
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/favorite")
    public ResponseResult listFavoritePost(Integer pageNum, Integer pageSize) {
        return postService.listFavoritePost(pageNum, pageSize);
    }

    /**
     * 修改帖子状态
     *
     * @param modifyPostStatusDto
     * @return
     */
    @PostMapping("/modify")
    public ResponseResult modifyPostStatus(@RequestBody ModifyPostStatusDto modifyPostStatusDto) {
        return postService.modifyPostStatus(modifyPostStatusDto);
    }


    @GetMapping("/matched")
    public ResponseResult getMatchRelationByPostId(Long postId){
        return postService.getMatchRelationByPostId(postId);
    }

}
