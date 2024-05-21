package com.chaos.team.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.team.domain.entity.TeamUser;
import com.chaos.team.mapper.TeamUserMapper;
import com.chaos.team.service.TeamUserService;
import org.springframework.stereotype.Service;

/**
 * (TeamUser)表服务实现类
 *
 * @author chaos
 * @since 2024-05-14 23:38:01
 */
@Service("teamUserService")
public class TeamUserServiceImpl extends ServiceImpl<TeamUserMapper, TeamUser> implements TeamUserService {

}

