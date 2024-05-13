package com.chaos.feign;

import com.chaos.feign.bo.AddPostGroupRelationBo;
import com.chaos.response.ResponseResult;
import com.chaos.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-05-12 21:58
 **/

/**
 * 社群远程调用模块
 */
@RestController
@RequiredArgsConstructor
public class GroupFeignController implements GroupFeignClient{
    private final GroupService groupService;

    @Override
    public ResponseResult addPostGroupRelation(AddPostGroupRelationBo addPostGroupRelationBo) {
        return groupService.addPostGroupRelation(addPostGroupRelationBo);
    }
}
