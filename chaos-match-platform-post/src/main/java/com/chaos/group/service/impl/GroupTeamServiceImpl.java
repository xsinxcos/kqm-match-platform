package com.chaos.group.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.group.domain.entity.GroupTeam;
import com.chaos.group.mapper.GroupTeamMapper;
import com.chaos.group.service.GroupTeamService;
import org.springframework.stereotype.Service;

/**
 * 队伍与社群的关系表(GroupTeam)表服务实现类
 *
 * @author chaos
 * @since 2024-05-15 01:55:17
 */
@Service("groupTeamService")
public class GroupTeamServiceImpl extends ServiceImpl<GroupTeamMapper, GroupTeam> implements GroupTeamService {

}

