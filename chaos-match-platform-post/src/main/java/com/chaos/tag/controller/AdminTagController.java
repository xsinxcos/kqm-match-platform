package com.chaos.tag.controller;

import com.chaos.annotation.AuthAdminCheck;
import com.chaos.annotation.SystemLog;
import com.chaos.response.ResponseResult;
import com.chaos.tag.domain.dto.AdminAddTagDto;
import com.chaos.tag.domain.dto.AdminDeleteTagDto;
import com.chaos.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-03-16 21:23
 **/

/**
 * 标签模块（管理端）
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/tag/manage")
public class AdminTagController {
    private final TagService tagService;

    /**
     * 管理端添加标签
     *
     * @param adminAddTagDto
     * @return
     */
    @AuthAdminCheck
    @PostMapping("/add")
    @SystemLog(BusinessName = "adminAddTag")
    public ResponseResult adminAddTag(@RequestBody @Valid AdminAddTagDto adminAddTagDto) {
        return tagService.adminAddTag(adminAddTagDto);
    }

    /**
     * 管理端删除标签
     *
     * @param dto
     * @return
     */
    @AuthAdminCheck
    @PostMapping("/delete")
    @SystemLog(BusinessName = "adminDeleteTag")
    public ResponseResult adminDeleteTag(@RequestBody @Valid AdminDeleteTagDto dto) {
        return tagService.adminDeleteTag(dto);
    }

    /**
     * 统计每种TAG对应的帖子的数量
     *
     * @return
     */
    @AuthAdminCheck
    @PostMapping("/count")
    @SystemLog(BusinessName = "adminPostTagCount")
    public ResponseResult adminPostTagCount() {
        return tagService.adminPostTagCount();
    }
}
