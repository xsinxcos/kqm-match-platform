package com.chaos.feign;

import com.chaos.feign.bo.AddPostUserMatchRelationBo;
import com.chaos.response.ResponseResult;
import com.chaos.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: PostFeignController
 * @author: xsinxcos
 * @create: 2024-02-12 01:37
 **/

/**
 * 帖子远程调用模块
 */
@RestController
@RequiredArgsConstructor
public class PostFeignController implements PostFeignClient {
    private final PostService postService;

    @Override
    public ResponseResult addPostUserMatchRelation(AddPostUserMatchRelationBo addPostUserMatchRelationBo) {
        return postService.addPostUserMatchRelation(addPostUserMatchRelationBo);
    }
}
