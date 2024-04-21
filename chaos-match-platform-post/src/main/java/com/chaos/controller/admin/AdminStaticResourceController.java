package com.chaos.controller.admin;

import com.chaos.annotation.AuthAdminCheck;
import com.chaos.annotation.SystemLog;
import com.chaos.domain.dto.admin.AdminAddRotatingPicDto;
import com.chaos.domain.dto.admin.AdminDeleteRotatingPicDto;
import com.chaos.response.ResponseResult;
import com.chaos.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-03-18 22:04
 **/

/**
 * 静态资源模块（管理端）
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/static/manage")
public class AdminStaticResourceController {
    private final ResourceService resourceService;

    /**
     * 管理端添加轮播图
     *
     * @param dto
     * @return
     */
    @AuthAdminCheck
    @PostMapping("/rotatingPic/add")
    @SystemLog(BusinessName = "adminAddRotatingPic")
    public ResponseResult adminAddRotatingPic(@RequestBody @Valid AdminAddRotatingPicDto dto) {
        return resourceService.adminAddRotatingPic(dto);
    }


    /**
     * 管理端删除轮播图
     *
     * @param dto
     * @return
     */
    @AuthAdminCheck
    @PostMapping("/rotatingPic/delete")
    @SystemLog(BusinessName = "adminDeleteRotatingPic")
    public ResponseResult adminDeleteRotatingPic(@RequestBody AdminDeleteRotatingPicDto dto) {
        return resourceService.adminDeleteRotatingPic(dto);
    }
}
