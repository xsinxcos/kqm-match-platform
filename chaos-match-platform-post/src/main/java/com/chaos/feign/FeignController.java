package com.chaos.feign;

import com.chaos.feign.PostFeignClient;
import com.chaos.feign.bo.AddPostGroupRelationBo;
import com.chaos.feign.bo.AddTeamUserMatchRelationBo;
import com.chaos.group.service.GroupService;
import com.chaos.response.ResponseResult;
import com.chaos.team.service.TeamService;
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
public class FeignController implements PostFeignClient {
    private final TeamService teamService;
    private final GroupService groupService;

    @Override
    public ResponseResult addPostGroupRelation(AddPostGroupRelationBo addPostGroupRelationBo) {
        return groupService.addPostGroupRelation(addPostGroupRelationBo);
    }

    @Override
    public ResponseResult addTeamUserMatchRelation(AddTeamUserMatchRelationBo addTeamUserMatchRelationBo) {
        return teamService.addTeamUserMatchRelation(addTeamUserMatchRelationBo);
    }
}
