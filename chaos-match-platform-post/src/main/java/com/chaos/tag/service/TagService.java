package com.chaos.tag.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaos.response.ResponseResult;
import com.chaos.tag.domain.dto.AdminAddTagDto;
import com.chaos.tag.domain.dto.AdminDeleteTagDto;
import com.chaos.tag.domain.entity.Tag;


/**
 * (Tag)表服务接口
 *
 * @author chaos
 * @since 2024-02-01 08:24:25
 */
public interface TagService extends IService<Tag> {

    ResponseResult adminAddTag(AdminAddTagDto adminAddTagDto);

    ResponseResult adminDeleteTag(AdminDeleteTagDto dto);

    ResponseResult adminPostTagCount();
}

