package com.chaos.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaos.domain.dto.admin.AdminAddTagDto;
import com.chaos.domain.dto.admin.AdminDeleteTagDto;
import com.chaos.domain.entity.Tag;
import com.chaos.response.ResponseResult;


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

