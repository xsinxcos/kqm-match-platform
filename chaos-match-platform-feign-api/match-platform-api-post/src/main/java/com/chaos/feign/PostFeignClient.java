package com.chaos.feign;

import com.chaos.feign.bo.AddPostUserMatchRelationBo;
import com.chaos.response.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @description: PostFeignClient
 * @author: xsinxcos
 * @create: 2024-02-12 01:40
 **/

/**
 * 帖子模块远程调用
 */
@FeignClient(value = "PostService")
public interface PostFeignClient {

    /**
     * 添加用户帖子匹配关系
     * @param addPostUserMatchRelationBo
     * @return
     */
    @PostMapping("/feign/addPostUserMatchRelation")
    ResponseResult addPostUserMatchRelation(@RequestBody AddPostUserMatchRelationBo addPostUserMatchRelationBo);
}
