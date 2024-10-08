package com.chaos.tag.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chaos.annotation.SystemLog;
import com.chaos.response.ResponseResult;
import com.chaos.tag.domain.entity.Tag;
import com.chaos.tag.domain.vo.TagListVo;
import com.chaos.tag.service.TagService;
import com.chaos.util.BeanCopyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description: TagController
 * @author: xsinxcos
 * @create: 2024-02-02 05:25
 **/

/**
 * 标签模块（APP端）
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/tag")
public class TagController {
    private final TagService tagService;

    /**
     * 展示所有标签
     *
     * @return
     */
    @GetMapping("/list")
    @SystemLog(BusinessName = "listTags")
    public ResponseResult listTags() {
        List<Tag> list = tagService.list(new LambdaQueryWrapper<>());
        List<TagListVo> tagListVos = BeanCopyUtils.copyBeanList(list, TagListVo.class);
        return ResponseResult.okResult(tagListVos);
    }
}
