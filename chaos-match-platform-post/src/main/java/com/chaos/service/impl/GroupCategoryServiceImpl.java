package com.chaos.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.domain.entity.GroupCategory;
import com.chaos.mapper.GroupCategoryMapper;
import org.springframework.stereotype.Service;
import com.chaos.service.GroupCategoryService;

/**
 * 社群与类别关系表(GroupCategory)表服务实现类
 *
 * @author chaos
 * @since 2024-05-13 20:52:29
 */
@Service("groupCategoryService")
public class GroupCategoryServiceImpl extends ServiceImpl<GroupCategoryMapper, GroupCategory> implements GroupCategoryService {

}

