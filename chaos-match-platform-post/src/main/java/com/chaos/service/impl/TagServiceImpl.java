package com.chaos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.domain.dto.admin.AdminAddTagDto;
import com.chaos.domain.dto.admin.AdminDeleteTagDto;
import com.chaos.domain.entity.Tag;
import com.chaos.domain.vo.admin.PostTagCountVo;
import com.chaos.mapper.PostTagMapper;
import com.chaos.mapper.TagMapper;
import com.chaos.response.ResponseResult;
import com.chaos.service.TagService;
import com.chaos.util.BeanCopyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * (Tag)表服务实现类
 *
 * @author chaos
 * @since 2024-02-01 08:24:25
 */
@Service("tagService")
@RequiredArgsConstructor
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
    private static final int TAG_DELETE = 1;

    private final PostTagMapper postTagMapper;

    @Override
    public ResponseResult adminAddTag(AdminAddTagDto adminAddTagDto) {
        Tag tag = BeanCopyUtils.copyBean(adminAddTagDto, Tag.class);
        save(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult adminDeleteTag(AdminDeleteTagDto dto) {
        //防御性编程
        Optional.ofNullable(dto.getId()).orElseThrow(() -> new RuntimeException("ID不能为空"));

        LambdaUpdateWrapper<Tag> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Objects.nonNull(dto.getId()), Tag::getId, dto.getId())
                .set(Tag::getDelFlag, TAG_DELETE);
        update(wrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult adminPostTagCount() {
        //获取全部标签
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        Map<Long, String> idName = list(wrapper).stream()
                .collect(Collectors.toMap(Tag::getId, Tag::getName, (oldValue, newValue) -> oldValue));
        //获取文章与标签关系
        List<Map<String, Long>> maps = postTagMapper.selectTagIdAndPostCount();
        List<PostTagCountVo> vos = new ArrayList<>();

        //包装成Vos
        maps.forEach(o -> {
            Long tagId = o.get("tag_id");
            String name = idName.get(tagId);
            if (Objects.nonNull(name)) {
                vos.add(new PostTagCountVo(tagId, name, o.get("post_count")));
            }
        });

        return ResponseResult.okResult(vos);
    }
}

