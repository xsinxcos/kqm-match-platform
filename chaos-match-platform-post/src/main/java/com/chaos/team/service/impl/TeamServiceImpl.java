package com.chaos.team.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.config.vo.PageVo;
import com.chaos.feign.UserFeignClient;
import com.chaos.feign.bo.AddTeamUserMatchRelationBo;
import com.chaos.feign.bo.AuthUserBo;
import com.chaos.feign.bo.PosterBo;
import com.chaos.group.domain.entity.GroupTeam;
import com.chaos.group.service.GroupTeamService;
import com.chaos.response.ResponseResult;
import com.chaos.team.domain.dto.ModifyTeamDetailDto;
import com.chaos.team.domain.entity.Team;
import com.chaos.team.domain.entity.TeamUser;
import com.chaos.team.domain.vo.GetMyTeamsVo;
import com.chaos.team.domain.vo.ListTeamByGroupId;
import com.chaos.team.domain.vo.TeamMemberVo;
import com.chaos.team.mapper.TeamMapper;
import com.chaos.team.mapper.TeamUserMapper;
import com.chaos.team.service.TeamService;
import com.chaos.team.service.TeamUserService;
import com.chaos.util.BeanCopyUtils;
import com.chaos.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * (Team)表服务实现类
 *
 * @author chaos
 * @since 2024-05-14 23:37:49
 */
@Service("teamService")
@RequiredArgsConstructor
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team> implements TeamService {
    private final TeamUserService teamUserService;

    private final GroupTeamService groupTeamService;

    private final UserFeignClient userFeignClient;

    private final TeamUserMapper teamUserMapper;

    @Override
    public ResponseResult addTeamUserMatchRelation(AddTeamUserMatchRelationBo addTeamUserMatchRelationBo) {
        //查找队伍
        Team team = getBaseMapper().selectOne(new LambdaQueryWrapper<Team>()
                .eq(Team::getPostId, addTeamUserMatchRelationBo.getPostId()));

        //将队伍与用户进行绑定
        List<Long> userIds = addTeamUserMatchRelationBo.getUserIds();
        List<TeamUser> teamUsers = new ArrayList<>();
        Long teamId = team.getId();
        userIds.forEach(o -> teamUsers.add(new TeamUser(teamId, o, 0)));

        teamUserMapper.saveIgnoreBatchTeamUser(teamUsers);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listTeamByGroupId(Integer pageNum, Integer pageSize, Long groupId) {
        LambdaQueryWrapper<GroupTeam> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GroupTeam::getGroupId ,groupId);
        Page<GroupTeam> page = new Page<>(pageNum ,pageSize);
        groupTeamService.page(page ,wrapper);

        List<Long> teamIds = page.getRecords().stream().map(GroupTeam::getTeamId).collect(Collectors.toList());
        List<Team> teams = getBaseMapper().selectBatchIds(teamIds);

        List<ListTeamByGroupId> vos = BeanCopyUtils.copyBeanList(teams, ListTeamByGroupId.class);

        return ResponseResult.okResult(new PageVo(vos ,page.getTotal()));
    }

    @Override
    public ResponseResult<?> getMyTeams(Integer pageNum, Integer pageSize) {
        Long userId = SecurityUtils.getUserId();
        //分页
        Page<TeamUser> page = new Page<>(pageNum ,pageSize);
        List<Long> teamIds = teamUserService.lambdaQuery()
                .eq(TeamUser::getUserId, userId)
                .page(page)
                .getRecords()
                .stream()
                .map(TeamUser::getTeamId)
                .collect(Collectors.toList());

        if(teamIds.isEmpty()) return ResponseResult.okResult(new PageVo(new ArrayList<>() ,0L));

        List<Team> teams = getBaseMapper().selectBatchIds(teamIds);

        List<GetMyTeamsVo> vos = new ArrayList<>();
        for (Team team : teams) {
            vos.add(GetMyTeamsVo.builder()
                    .id(team.getId())
                    .name(team.getName())
                    .icon(team.getIcon())
                    .build());
        }
        return ResponseResult.okResult(new PageVo(vos ,page.getTotal()));
    }

    @Override
    public ResponseResult<?> modifyTeamDetail(ModifyTeamDetailDto dto) {
        //todo 鉴权
        Team team = getById(dto.getId());
        Optional.ofNullable(dto.getName())
                .ifPresent(team::setName);
        Optional.ofNullable(dto.getIcon()).ifPresent(team::setIcon);

        updateById(team);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<?> teamMembersList(Long teamId) {
        List<Long> userIds = teamUserService.lambdaQuery()
                .eq(TeamUser::getTeamId, teamId)
                .list()
                .stream()
                .map(TeamUser::getUserId)
                .collect(Collectors.toList());

        Map<Long, AuthUserBo> boMap = userFeignClient.getBatchUserByUserIds(userIds).getData();

        List<TeamMemberVo> vos = new ArrayList<>();
        for (Long userId : userIds) {
            AuthUserBo bo = boMap.get(userId);
            TeamMemberVo vo = TeamMemberVo.builder()
                    .id(userId)
                    .userName(bo.getUserName())
                    .avatar(bo.getAvatar())
                    .selfLabel(bo.getSelfLabel())
                    .build();
            vos.add(vo);
        }
        return ResponseResult.okResult(vos);
    }
}

