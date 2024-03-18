package com.chaos.controller.admin;

import com.chaos.annotation.AuthAdminCheck;
import com.chaos.annotation.SystemLog;
import com.chaos.domain.dto.admin.AdminAddTagDto;
import com.chaos.domain.dto.admin.AdminDeleteTagDto;
import com.chaos.response.ResponseResult;
import com.chaos.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-03-16 21:23
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/tag/manage")
public class AdminTagController {
    private final TagService tagService;

    /**
     * 管理员添加TAG
     * @param adminAddTagDto
     * @return
     */
    @AuthAdminCheck
    @PostMapping("/add")
    @SystemLog(BusinessName = "adminAddTag")
    public ResponseResult adminAddTag(@RequestBody AdminAddTagDto adminAddTagDto){
        return tagService.adminAddTag(adminAddTagDto);
    }


    @AuthAdminCheck
    @PostMapping("/delete")
    @SystemLog(BusinessName = "adminDeleteTag")
    public ResponseResult adminDeleteTag(@RequestBody AdminDeleteTagDto dto){
        return tagService.adminDeleteTag(dto);
    }

}
