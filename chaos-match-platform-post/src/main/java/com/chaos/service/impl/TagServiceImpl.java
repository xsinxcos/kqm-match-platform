package com.chaos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.domain.dto.admin.AdminAddTagDto;
import com.chaos.domain.dto.admin.AdminDeleteTagDto;
import com.chaos.domain.entity.Tag;
import com.chaos.mapper.TagMapper;
import com.chaos.response.ResponseResult;
import com.chaos.service.TagService;
import com.chaos.util.BeanCopyUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * (Tag)表服务实现类
 *
 * @author chaos
 * @since 2024-02-01 08:24:25
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
    private static final int TAG_DELETE = 1;

    @Override
    public ResponseResult adminAddTag(AdminAddTagDto adminAddTagDto) {
        Tag tag = BeanCopyUtils.copyBean(adminAddTagDto, Tag.class);
        save(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult adminDeleteTag(AdminDeleteTagDto dto) {
        //防御性编程
        Optional.ofNullable(dto.getId()).orElseThrow(() -> new RuntimeException("TagID不能为空"));

        LambdaUpdateWrapper<Tag> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Objects.nonNull(dto.getId()) ,Tag::getId ,dto.getId())
                .set(Tag::getDelFlag ,TAG_DELETE);
        update(wrapper);
        return ResponseResult.okResult();
    }
}

