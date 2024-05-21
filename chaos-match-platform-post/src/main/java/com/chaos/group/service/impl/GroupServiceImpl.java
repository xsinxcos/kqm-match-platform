package com.chaos.group.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.category.domain.bo.CategoryBo;
import com.chaos.category.domain.entity.Category;
import com.chaos.category.service.CategoryService;
import com.chaos.config.vo.PageVo;
import com.chaos.feign.UserFeignClient;
import com.chaos.feign.bo.AddPostGroupRelationBo;
import com.chaos.feign.bo.AuthUserBo;
import com.chaos.group.domain.bo.GroupOwnerBo;
import com.chaos.group.domain.dto.CreateGroupDto;
import com.chaos.group.domain.dto.ListGroupDto;
import com.chaos.group.domain.dto.ModifyGroupDetailDto;
import com.chaos.group.domain.entity.Group;
import com.chaos.group.domain.entity.GroupTeam;
import com.chaos.group.domain.entity.GroupUser;
import com.chaos.group.domain.vo.TeamListInGroupVo;
import com.chaos.group.domain.vo.GroupDetailVo;
import com.chaos.group.domain.vo.GroupListVo;
import com.chaos.group.mapper.GroupMapper;
import com.chaos.group.mapper.GroupUserMapper;
import com.chaos.group.service.GroupService;
import com.chaos.group.service.GroupTeamService;
import com.chaos.group.service.GroupUserService;
import com.chaos.response.ResponseResult;
import com.chaos.team.domain.entity.Team;
import com.chaos.team.domain.entity.TeamUser;
import com.chaos.team.service.TeamService;
import com.chaos.team.service.TeamUserService;
import com.chaos.usersign.domain.entity.UserSignin;
import com.chaos.usersign.domain.vo.SigninRankVo;
import com.chaos.usersign.service.UserSigninService;
import com.chaos.util.BeanCopyUtils;
import com.chaos.util.SecurityUtils;
import com.chaos.util.SnowFlakeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * 小社区（社群）(Group)表服务实现类
 *
 * @author chaos
 * @since 2024-05-09 21:09:08
 */
@Service("groupService")
@RequiredArgsConstructor
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements GroupService {

    private final GroupUserService groupUserService;

    private final GroupUserMapper groupUserMapper;

    private final CategoryService categoryService;

    private final TeamService teamService;

    private final TeamUserService teamUserService;

    private final UserSigninService userSigninService;

    private final GroupTeamService groupTeamService;

    private final static int GROUP_USER_SUPER_ADMIN = 2;

    private final static int GROUP_USER_COMMON_USER = 0;

    private final UserFeignClient userFeignClient;

    private static final int USER_POST_MATCH_STATUS = 0;

    @Override
    public ResponseResult createGroup(CreateGroupDto dto) {
        //获取分布式ID
        Long flakeId = SnowFlakeUtil.getDefaultSnowFlakeId();
        Group newGroup = Group.builder()
                .id(flakeId)
                .label(dto.getLabel())
                .name(dto.getName())
                .icon(dto.getIcon())
                .categoryId(dto.getCategoryId())
                .build();
        save(newGroup);
        //保存用户与社群的关系
        groupUserService.save(
                GroupUser.builder()
                        .groupId(newGroup.getId())
                        .userId(SecurityUtils.getUserId())
                        .type(GROUP_USER_SUPER_ADMIN)
                        .build()
        );
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult groupDetail(Long groupId) {
        Group group = getById(groupId);
        //查询现群主
        GroupUser groupUser = groupUserService.getBaseMapper().selectOne((
                new LambdaQueryWrapper<GroupUser>()
                        .eq(GroupUser::getGroupId, groupId)
                        .eq(GroupUser::getType, GROUP_USER_SUPER_ADMIN)));

        Long userId = groupUser.getUserId();
        //查询现群主详细信息
        CompletableFuture<AuthUserBo> authUserBoCompletableFuture = CompletableFuture.supplyAsync(() -> {
            return userFeignClient.getUserById(userId).getData();
        });
        //查询社群的类别信息
        CompletableFuture<Category> groupCategoryFuture = CompletableFuture.supplyAsync(() -> {
            return categoryService.getById(group.getCategoryId());
        });
        Category category = null;
        AuthUserBo authUserBo = null;
        try {
            category = groupCategoryFuture.get();
            authUserBo = authUserBoCompletableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        //生成VO
        GroupDetailVo vo = BeanCopyUtils.copyBean(group, GroupDetailVo.class);
        vo.setOwner(GroupOwnerBo.builder()
                .uid(authUserBo.getId())
                .sex(authUserBo.getSex())
                .avatar(authUserBo.getAvatar())
                .selfLabel(authUserBo.getSelfLabel())
                .username(authUserBo.getUserName())
                .build());
        vo.setCategory(new CategoryBo(category.getId(), category.getName()));
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult modifyGroupDetail(ModifyGroupDetailDto dto) {
        //检验是否为社群超级管理员
        GroupUser groupUser = groupUserService.getBaseMapper().selectOne(
                new LambdaQueryWrapper<GroupUser>()
                        .eq(GroupUser::getGroupId, dto.getId()));
        if (Objects.isNull(groupUser) || !groupUser.getUserId().equals(SecurityUtils.getUserId())) {
            throw new RuntimeException("修改失败");
        }

        //进行修改
        Group group = getById(dto.getId());
        group.setName(dto.getName());
        group.setIcon(dto.getIcon());
        group.setLabel(dto.getLabel());
        group.setIntroduction(dto.getIntroduction());
        group.setCategoryId(dto.getCategoryId());

        //更新社群基本信息
        updateById(group);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addPostGroupRelation(AddPostGroupRelationBo bo) {
        Team team = teamService.getBaseMapper().selectOne(new LambdaQueryWrapper<Team>().eq(
                Team::getPostId, bo.getPostId()
        ));
        //获取帖子对应队伍的成员
        List<Long> userIds = teamUserService.list(new LambdaQueryWrapper<TeamUser>()
                        .eq(TeamUser::getTeamId, team.getId()))
                .stream()
                .map(TeamUser::getUserId)
                .collect(Collectors.toList());
        Long groupId = bo.getGroupId();
        //将队伍成员与群组进行关联，默认普通用户
        List<GroupUser> groupUserList = new ArrayList<>();
        userIds.forEach(o -> groupUserList.add(
                GroupUser.builder()
                        .groupId(groupId)
                        .userId(o)
                        .type(GROUP_USER_COMMON_USER)
                        .createTime(new Date())
                        .createTime(new Date())
                        .build()
        ));
        //批量保存
        groupUserMapper.saveIgnoreBatchGroupUser(groupUserList);


        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listGroup(ListGroupDto dto) {
        LambdaQueryWrapper<Group> wrapper = new LambdaQueryWrapper<>();
        //筛选
        wrapper.eq(Objects.nonNull(dto.getCategoryId()), Group::getCategoryId, dto.getCategoryId())
                .like(Objects.nonNull(dto.getQuery()) ,Group::getName ,dto.getQuery())
                .orderByDesc(Group::getId);
        //分页
        Page<Group> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        page(page, wrapper);
        //封装VO
        List<GroupListVo> vos = BeanCopyUtils.copyBeanList(page.getRecords(), GroupListVo.class);
        //获取成员数
        for (GroupListVo record : vos) {
            Integer count = groupUserMapper.selectCount(
                    new LambdaQueryWrapper<GroupUser>()
                            .eq(GroupUser::getGroupId, record.getId()));
            record.setMembersCount(Long.valueOf(count));
        }
        return ResponseResult.okResult(new PageVo(vos, page.getTotal()));
    }

    @Override
    public ResponseResult getMyGroup(Integer pageNum, Integer pageSize) {
        Long userId = SecurityUtils.getUserId();
        //查找自己的社群
        LambdaQueryWrapper<GroupUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GroupUser::getUserId, userId);
        //分页
        Page<GroupUser> page = new Page<>(pageNum, pageSize);
        groupUserService.page(page, wrapper);

        List<GroupUser> records = page.getRecords();
        List<Long> groupIds = records.stream().map(GroupUser::getGroupId).collect(Collectors.toList());

        if(groupIds.isEmpty()) return ResponseResult.okResult(new PageVo(new ArrayList<>() ,0L));

        List<Group> groups = getBaseMapper().selectBatchIds(groupIds);
        //获取成员数
        Map<Long, Integer> groupMembers = getGroupMembers(groupIds);

        List<GroupListVo> vos = BeanCopyUtils.copyBeanList(groups, GroupListVo.class);

        vos.forEach(o -> o.setMembersCount(Long.valueOf(groupMembers.get(o.getId()))));

        return ResponseResult.okResult(new PageVo(vos, page.getTotal()));
    }

    @Override
    public ResponseResult<?> getTeamRankInGroup(Long groupId) {
        List<UserSignin> userSignins = userSigninService.lambdaQuery().eq(UserSignin::getGroupId, groupId).list();
        //获取总队伍的排名
        Map<Long, Long> map = new TreeMap<>();
        userSignins.forEach(o -> map.put(o.getTeamId(), map.getOrDefault(o.getTeamId(), 0L) + 1));
        //选出前十
        PriorityQueue<long[]> rank = new PriorityQueue<>((o1, o2) -> (int) (o2[1] - o1[1]));
        for (Map.Entry<Long, Long> entry : map.entrySet()) {
            rank.add(new long[]{entry.getKey(), entry.getValue()});
        }
        //获取前十名的选手ID
        List<Long> teamIds = new ArrayList<>();
        int n = 10;
        for (long[] pair : rank) {
            if (n-- == 0) break;
            teamIds.add(pair[0]);
        }
        if(teamIds.isEmpty()){
            return ResponseResult.okResult(new PageVo(new ArrayList<>(), 0L));
        }
        List<Team> teams = teamService.getBaseMapper().selectBatchIds(teamIds);
        List<SigninRankVo> vos = BeanCopyUtils.copyBeanList(teams, SigninRankVo.class);
        vos.forEach(o -> o.setCount(map.get(o.getId())));
        //进行排序
        vos.sort(((o1, o2) -> Math.toIntExact(o2.getCount() - o1.getCount())));
        return ResponseResult.okResult(new PageVo(vos, (long) map.size()));
    }

    @Override
    public ResponseResult<?> quitGroup(Long groupId) {
        Long userId = SecurityUtils.getUserId();
        groupUserService.getBaseMapper().delete(
                new LambdaQueryWrapper<GroupUser>()
                        .eq(GroupUser::getGroupId, groupId)
                        .eq(GroupUser::getUserId, userId));
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<?> listTeamInGroup(Long groupId) {
        List<Long> teamIds = groupTeamService.lambdaQuery()
                .eq(GroupTeam::getGroupId, groupId)
                .list()
                .stream()
                .map(GroupTeam::getTeamId)
                .collect(Collectors.toList());

        if(teamIds.isEmpty()){
            return ResponseResult.okResult(new PageVo(new ArrayList<>() ,0L));
        }
        List<Team> teams = teamService.getBaseMapper().selectBatchIds(teamIds);

        Map<Long, Integer> teamMembers = getTeamMembers(teamIds);
        List<TeamListInGroupVo> vos = new ArrayList<>();
        for (Team team : teams) {
            TeamListInGroupVo vo = TeamListInGroupVo.builder()
                    .id(team.getId())
                    .name(team.getName())
                    .icon(team.getIcon())
                    .teamNumber(teamMembers.get(team.getId()))
                    .build();
            vos.add(vo);
        }

        return ResponseResult.okResult(new PageVo(vos ,(long)vos.size()));
    }

    private Map<Long, Integer> getGroupMembers(List<Long> groupIds) {
        Map<Long, Integer> groupMembersMap = new HashMap<>();
        for (Long groupId : groupIds) {
            Integer count = groupUserMapper.selectCount(
                    new LambdaQueryWrapper<GroupUser>()
                            .eq(GroupUser::getGroupId, groupId));
            groupMembersMap.put(groupId, count);
        }
        return groupMembersMap;
    }

    private Map<Long ,Integer> getTeamMembers(List<Long> teamIds){
        Map<Long, Integer> teamMembersMap = new HashMap<>();
        for (Long teamId : teamIds) {
            Integer count = teamUserService.lambdaQuery().eq(TeamUser::getTeamId, teamId)
                    .count();
            if(count != 0)
                teamMembersMap.put(teamId ,count);
        }
        return teamMembersMap;
    }

}

