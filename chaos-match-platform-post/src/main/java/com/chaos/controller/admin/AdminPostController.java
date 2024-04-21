package com.chaos.controller.admin;

import com.chaos.annotation.AuthAdminCheck;
import com.chaos.annotation.SystemLog;
import com.chaos.domain.dto.admin.AdminDeletePostDto;
import com.chaos.domain.dto.admin.AdminListPostDto;
import com.chaos.response.ResponseResult;
import com.chaos.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-03-16 21:22
 **/

/**
 * 帖子模块（管理端）
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/post/manage")
public class AdminPostController {
    private final PostService postService;

    /**
     * 管理端罗列帖子
     *
     * @param adminListPostDto
     * @return
     */
    @AuthAdminCheck
    @PostMapping("/list")
    @SystemLog(BusinessName = "adminListPost")
    public ResponseResult adminListPost(@RequestBody @Valid AdminListPostDto adminListPostDto) {
        return postService.adminListPost(adminListPostDto);
    }

    /**
     * 管理端删除帖子
     *
     * @param adminDeletePostDto
     * @return
     */
    @AuthAdminCheck
    @PostMapping("/delete")
    @SystemLog(BusinessName = "adminDeletePost")
    public ResponseResult adminDeletePost(@RequestBody @Valid AdminDeletePostDto adminDeletePostDto) {
        return postService.adminDeletePost(adminDeletePostDto);
    }
}
