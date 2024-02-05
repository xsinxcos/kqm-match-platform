package com.chaos.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chaos.domain.entity.Tag;
import com.chaos.domain.vo.TagListVo;
import com.chaos.response.ResponseResult;
import com.chaos.service.TagService;
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
    public ResponseResult listTags() {
        List<Tag> list = tagService.list(new LambdaQueryWrapper<>());
        List<TagListVo> tagListVos = BeanCopyUtils.copyBeanList(list, TagListVo.class);
        return ResponseResult.okResult(tagListVos);
    }
}
