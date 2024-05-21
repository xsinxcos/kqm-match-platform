package com.chaos.staticResource.controller;

import com.chaos.response.ResponseResult;
import com.chaos.staticResource.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-03-18 21:41
 **/

/**
 * 静态资源模块（APP端）
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/static")
public class StaticResourceController {

    private final ResourceService resourceService;

    /**
     * 获取轮播图列表
     *
     * @return
     */
    @GetMapping("/rotatingPic")
    public ResponseResult getRotatingPic() {
        return resourceService.getRotatingPic();
    }
}
