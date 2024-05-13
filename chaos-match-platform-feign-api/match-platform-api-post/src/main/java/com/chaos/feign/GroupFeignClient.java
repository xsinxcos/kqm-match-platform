package com.chaos.feign;

import com.chaos.feign.bo.AddPostGroupRelationBo;
import com.chaos.response.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-05-12 21:51
 **/

/**
 * 社群远程调用模块
 */
@FeignClient(value = "GroupService")
public interface GroupFeignClient {
    /**
     * 添加群组与帖子的关系
     * @param addPostGroupRelationBo
     * @return
     */
    @PostMapping("/feign/addPostGroupRelation")
    ResponseResult addPostGroupRelation(@RequestBody AddPostGroupRelationBo addPostGroupRelationBo);
}
