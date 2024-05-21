package com.chaos.group.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.group.domain.entity.GroupUser;
import com.chaos.group.mapper.GroupUserMapper;
import com.chaos.group.service.GroupUserService;
import org.springframework.stereotype.Service;

/**
 * 社群与用户关系表(GroupUser)表服务实现类
 *
 * @author chaos
 * @since 2024-05-09 21:23:46
 */
@Service("groupUserService")
public class GroupUserServiceImpl extends ServiceImpl<GroupUserMapper, GroupUser> implements GroupUserService {

}

