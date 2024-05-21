package com.chaos.feign;

import com.chaos.feign.bo.AddPostGroupRelationBo;
import com.chaos.feign.bo.AddTeamUserMatchRelationBo;
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
     * 添加用户队伍关系
     * @param addTeamUserMatchRelationBo
     * @return
     */
    @PostMapping("/feign/addTeamUserMatchRelation")
    ResponseResult addTeamUserMatchRelation(@RequestBody AddTeamUserMatchRelationBo addTeamUserMatchRelationBo);

    /**
     * 添加群组与帖子的关系
     * @param addPostGroupRelationBo
     * @return
     */
    @PostMapping("/feign/addPostGroupRelation")
    ResponseResult addPostGroupRelation(@RequestBody AddPostGroupRelationBo addPostGroupRelationBo);
}
