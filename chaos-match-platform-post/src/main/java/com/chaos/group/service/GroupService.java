package com.chaos.group.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaos.feign.bo.AddPostGroupRelationBo;
import com.chaos.group.domain.dto.CreateGroupDto;
import com.chaos.group.domain.dto.ListGroupDto;
import com.chaos.group.domain.dto.ModifyGroupDetailDto;
import com.chaos.group.domain.entity.Group;
import com.chaos.response.ResponseResult;


/**
 * 小社区（社群）(Group)表服务接口
 *
 * @author chaos
 * @since 2024-05-09 21:09:08
 */
public interface GroupService extends IService<Group> {

    ResponseResult createGroup(CreateGroupDto dto);

    ResponseResult groupDetail(Long groupId);

    ResponseResult modifyGroupDetail(ModifyGroupDetailDto dto);

    ResponseResult addPostGroupRelation(AddPostGroupRelationBo addPostGroupRelationBo);

    ResponseResult listGroup(ListGroupDto dto);

    ResponseResult getMyGroup(Integer pageNum, Integer pageSize);

    ResponseResult<?> getTeamRankInGroup(Long groupId);

    ResponseResult<?> quitGroup(Long groupId);

    ResponseResult<?> listTeamInGroup(Long groupId);
}

