package com.chaos.controller.app;

import com.chaos.response.ResponseResult;
import com.chaos.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-03-18 21:41
 **/
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
