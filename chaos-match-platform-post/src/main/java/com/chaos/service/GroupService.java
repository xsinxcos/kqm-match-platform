package com.chaos.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaos.domain.dto.app.CreateGroupDto;
import com.chaos.domain.dto.app.ModifyGroupDetailDto;
import com.chaos.domain.entity.Group;
import com.chaos.feign.bo.AddPostGroupRelationBo;
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
}

