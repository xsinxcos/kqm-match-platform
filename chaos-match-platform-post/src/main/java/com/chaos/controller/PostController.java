package com.chaos.controller;

import com.chaos.domain.dto.AddPostDto;
import com.chaos.domain.dto.ModifyMyPostDto;
import com.chaos.response.ResponseResult;
import com.chaos.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
     * 罗列帖子
     *
     * @param pageNum
     * @param pageSize
     * @param tagId
     * @return
     */
    @GetMapping("/list")
    public ResponseResult listPost(Integer pageNum, Integer pageSize, Long tagId) {
        return postService.listPost(pageNum, pageSize, tagId);
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
}
