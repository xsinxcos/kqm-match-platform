package com.chaos.controller.admin;

import com.chaos.annotation.AuthAdminCheck;
import com.chaos.annotation.SystemLog;
import com.chaos.domain.dto.admin.AdminDeleteCommentDto;
import com.chaos.response.ResponseResult;
import com.chaos.service.CommentService;
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
 * 评论模块（管理端）
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/comment/manage")
public class AdminCommentController {

    private final CommentService commentService;

    /**
     * 管理端删除评论
     *
     * @param dto
     * @return
     */
    @AuthAdminCheck
    @PostMapping("/delete")
    @SystemLog(BusinessName = "adminDeleteComment")
    public ResponseResult adminDeleteComment(@RequestBody @Valid AdminDeleteCommentDto dto) {
        return commentService.adminDeleteComment(dto);
    }

}
