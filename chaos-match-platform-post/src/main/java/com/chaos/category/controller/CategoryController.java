package com.chaos.category.controller;

import com.chaos.annotation.SystemLog;
import com.chaos.category.service.CategoryService;
import com.chaos.response.ResponseResult;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-05-13 18:34
 **/

/**
 * 类别模块（APP端）
 */
@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 罗列社群类别种类
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    @SystemLog(BusinessName = "listCategory")
    public ResponseResult listCategory(@NonNull Integer pageNum, @NonNull Integer pageSize) {
        return categoryService.listCategory(pageNum, pageSize);
    }
}
