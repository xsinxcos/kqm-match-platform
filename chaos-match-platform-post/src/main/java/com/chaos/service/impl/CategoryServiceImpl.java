package com.chaos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.config.vo.PageVo;
import com.chaos.domain.entity.Category;
import com.chaos.domain.vo.app.CategoryVo;
import com.chaos.mapper.CategoryMapper;
import com.chaos.response.ResponseResult;
import com.chaos.service.CategoryService;
import com.chaos.util.BeanCopyUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分类表(Category)表服务实现类
 *
 * @author chaos
 * @since 2024-05-13 01:26:58
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {


    @Override
    public ResponseResult listCategory(Integer pageNum, Integer pageSize) {
        Page<Category> page = new Page<>(pageNum ,pageSize);
        page(page ,new LambdaQueryWrapper<>());

        List<Category> records = page.getRecords();
        List<CategoryVo> vos = BeanCopyUtils.copyBeanList(records, CategoryVo.class);

        return ResponseResult.okResult(new PageVo(vos ,page.getTotal()));
    }
}

