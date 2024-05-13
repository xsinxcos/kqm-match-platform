package com.chaos.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaos.domain.entity.Category;
import com.chaos.response.ResponseResult;


/**
 * 分类表(Category)表服务接口
 *
 * @author chaos
 * @since 2024-05-13 01:26:57
 */
public interface CategoryService extends IService<Category> {

    ResponseResult listCategory(Integer pageNum, Integer pageSize);
}

