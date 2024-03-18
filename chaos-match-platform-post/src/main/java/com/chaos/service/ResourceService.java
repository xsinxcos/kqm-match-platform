package com.chaos.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaos.domain.dto.admin.AdminAddRotatingPicDto;
import com.chaos.domain.dto.admin.AdminDeleteRotatingPicDto;
import com.chaos.domain.entity.Resource;
import com.chaos.response.ResponseResult;


/**
 * 用户端静态资源(Resource)表服务接口
 *
 * @author chaos
 * @since 2024-03-18 21:30:54
 */
public interface ResourceService extends IService<Resource> {

    ResponseResult getRotatingPic();

    ResponseResult adminAddRotatingPic(AdminAddRotatingPicDto dto);

    ResponseResult adminDeleteRotatingPic(AdminDeleteRotatingPicDto dto);
}

