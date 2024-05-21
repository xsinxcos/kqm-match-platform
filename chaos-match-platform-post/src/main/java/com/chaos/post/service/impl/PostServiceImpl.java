package com.chaos.post.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.common.util.MeiliSearchUtils;
import com.chaos.common.util.TimeUtils;
import com.chaos.config.vo.PageVo;
import com.chaos.constant.AppHttpCodeEnum;
import com.chaos.entity.LoginUser;
import com.chaos.entity.User;
import com.chaos.feign.UserFeignClient;
import com.chaos.feign.bo.AuthUserBo;
import com.chaos.feign.bo.PosterBo;
import com.chaos.group.domain.entity.GroupTeam;
import com.chaos.group.domain.entity.GroupUser;
import com.chaos.group.service.GroupTeamService;
import com.chaos.group.service.GroupUserService;
import com.chaos.post.domain.bo.PostBo;
import com.chaos.post.domain.bo.SearchPostBo;
import com.chaos.post.domain.dto.*;
import com.chaos.post.domain.entity.Post;
import com.chaos.post.domain.entity.PostGroup;
import com.chaos.post.domain.entity.PostTag;
import com.chaos.post.domain.entity.PostUser;
import com.chaos.post.domain.vo.*;
import com.chaos.post.mapper.PostMapper;
import com.chaos.post.mapper.PostTagMapper;
import com.chaos.post.service.PostGroupService;
import com.chaos.post.service.PostService;
import com.chaos.post.service.PostTagService;
import com.chaos.post.service.PostUserService;
import com.chaos.recommend.RecommendDataModel;
import com.chaos.response.ResponseResult;
import com.chaos.tag.domain.bo.TagBo;
import com.chaos.tag.domain.entity.Tag;
import com.chaos.tag.service.TagService;
import com.chaos.team.domain.entity.Team;
import com.chaos.team.domain.entity.TeamUser;
import com.chaos.team.service.TeamService;
import com.chaos.team.service.TeamUserService;
import com.chaos.usersign.domain.entity.UserSignin;
import com.chaos.usersign.service.UserSigninService;
import com.chaos.util.BeanCopyUtils;
import com.chaos.util.SecurityUtils;
import com.chaos.util.SnowFlakeUtil;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import com.meilisearch.sdk.SearchRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * 帖子表(Post)表服务实现类
 *
 * @author chaos
 * @since 2024-02-01 07:56:43
 */
@Service("postService")
@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {
    private static final int FAVORITE_STATUS = 1;

    private static final int POST_STATUS_MATCHING = 0;
    private static final int POST_STATUS_MATCH_COMPLETE = 1;
    private static final int POST_STATUS_HAND_UP = 2;
    private static final int POST_DELETE = 1;
    private static final int LIST_CONTENT_CORP_LENGTH = 20;
    private static final String POST_INDEX_UID = "post";

    private static final Integer POST_TYPE_LIVE = 1;

    private static final Integer POST_DISPLAY_PUBLIC = 0;

    private static final Integer POST_DISPLAY_GROUP = 1;


    private final UserFeignClient userFeignClient;

    private final PostTagService postTagService;

    private final TagService tagService;

    private final PostUserService postUserService;

    private final GroupTeamService groupTeamService;

    private final MeiliSearchUtils meiliSearchUtils;

    private final PostGroupService postGroupService;

    private final RecommendDataModel recommendDataModel;

    private final GroupUserService groupUserService;

    private final PostTagMapper postTagMapper;

    private final TeamService teamService;

    private final TeamUserService teamUserService;

    private final UserSigninService userSigninService;

    @Override
    @Transactional
    public ResponseResult addPost(AddPostDto addPostDto) {
        Post post = BeanCopyUtils.copyBean(addPostDto, Post.class);
        List<TagBo> tags = addPostDto.getTags();

        saveMatchPostToDb(tags ,post ,0);
        //将帖子存入MeiliSearch中
        PostBo postBo = BeanCopyUtils.copyBean(post, PostBo.class);
        postBo.setStatus(POST_STATUS_MATCHING);
        postBo.setTags(tags);
        postBo.setBeginTimeStamp(postBo.getBeginTime().getTime());
        postBo.setEndTimeStamp(postBo.getEndTime().getTime());

        meiliSearchUtils.addDocumentByIndex(postBo, POST_INDEX_UID);

        Team team = Team.builder()
                .id(SnowFlakeUtil.getDefaultSnowFlakeId())
                .postId(post.getId())
                .build();
        //同时生成队伍
        teamService.save(team);

        return ResponseResult.okResult();
    }


    @Override
    public ResponseResult listPost(ListPostDto listPostDto) {
        SearchPostBo searchPostBo = BeanCopyUtils.copyBean(listPostDto, SearchPostBo.class);
        searchPostBo.setStatus(POST_STATUS_MATCHING);
        return searchPost(searchPostBo);
    }

    @Override
    public ResponseResult showPost(Long postId) {
        Post post = getById(postId);
        PostShowVo postShowVo = null;
        try {
            postShowVo = getPostShowVo(post);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        postShowVo.setIsKeep(false);
        //若登录则获取用户信息
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (Objects.nonNull(loginUser)) {
            User user = loginUser.getUser();
            //查询用户是否收藏
            LambdaQueryWrapper<PostUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(PostUser::getPostId, postId)
                    .eq(PostUser::getUserId, user.getId())
                    .eq(PostUser::getStatus, FAVORITE_STATUS);
            PostUser one = postUserService.getOne(wrapper);
            //若收藏则将vo中isKeep修改成true
            if (Objects.nonNull(one)) {
                postShowVo.setIsKeep(true);
            }
        }
        return ResponseResult.okResult(postShowVo);
    }

    @Override
    public ResponseResult getMyPost(Integer pageNum, Integer pageSize) {
        //获取用户的userid
        Long userId = SecurityUtils.getUserId();
        //构建分页器
        Page<Post> postPage = new Page<>(pageNum, pageSize);
        //获取发帖人信息
        List<Post> records = lambdaQuery().eq(Post::getPosterId, userId)
                .orderByDesc(Post::getId)
                .page(postPage).getRecords();

        List<PostListVo> vos = null;
        try {
            vos = getPostListVo(records);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("获取失败");
        }
        return ResponseResult.okResult(new PageVo(vos, (long)vos.size()));
    }

    @Override
    @Transactional
    public ResponseResult modifyMyPost(ModifyMyPostDto modifyMyPostDto) {
        Post post = getById(modifyMyPostDto.getId());
        //检验帖子是否属于该用户
        if (!post.getPosterId().equals(SecurityUtils.getUserId())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.ERROR);
        }

        //截取文章，将图片不纳入审核
        String content = modifyMyPostDto.getContent();
        String[] split = content.split("\\*\\*/img/\\*\\*");
        //对帖子标题和内容进行审查屏蔽
        content = SensitiveWordHelper.replace(split[0], '*');

        //将屏蔽后的图片重新拼接
        //将屏蔽后的图片重新拼接
        if (split.length > 1) {
            content += "**/img/**";
            content += split[1];
        }

        //更新帖子信息
        post.setTitle(SensitiveWordHelper.replace(modifyMyPostDto.getTitle()));
        post.setContent(content);
        post.setLatitude(modifyMyPostDto.getLatitude());
        post.setLongitude(modifyMyPostDto.getLongitude());
        post.setMeetAddress(modifyMyPostDto.getMeetAddress());
        post.setBeginTime(modifyMyPostDto.getBeginTime());
        post.setEndTime(modifyMyPostDto.getEndTime());
        updateById(post);

        //删除原有tag与帖子的关系
        postTagService.getBaseMapper().delete(
                new QueryWrapper<PostTag>().eq("post_id", post.getId())
        );

        //重新添加tag与帖子的关系
        List<PostTag> postTags = new ArrayList<>();
        for (TagBo tag : modifyMyPostDto.getTags()) {
            postTags.add(new PostTag(post.getId(), tag.getId()));
        }
        postTagService.saveBatch(postTags);

        if(post.getDisplayLocation().equals(0)) {
            //更新meilisearch中的document
            PostBo postBo = BeanCopyUtils.copyBean(post, PostBo.class);
            postBo.setTags(modifyMyPostDto.getTags());
            postBo.setBeginTimeStamp(postBo.getBeginTime().getTime());
            postBo.setEndTimeStamp(postBo.getEndTime().getTime());

            meiliSearchUtils.updateDocumentByIndex(postBo, POST_INDEX_UID);

        }
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult deleteMyPost(Long id) {
        //检查帖子所属人
        Post byId = getById(id);
        if (Objects.isNull(byId) || !byId.getPosterId().equals(SecurityUtils.getUserId())) {
            throw new RuntimeException(AppHttpCodeEnum.NO_OPERATOR_AUTH.getMsg());
        }
        deletePostById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addFavoritePost(AddFavoritePostDto addFavoritePostDto) {
        Long userId = SecurityUtils.getUserId();
        Long id = addFavoritePostDto.getId();
        Post byId = getById(id);
        if (Objects.isNull(byId)) {
            throw new RuntimeException("帖子不存在");
        }
        postUserService.getBaseMapper().insert(new PostUser(addFavoritePostDto.getId(), userId, FAVORITE_STATUS));
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteFavoritePost(DeleteFavoritePostDto dto) {
        LambdaQueryWrapper<PostUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PostUser::getPostId, dto.getId())
                .eq(PostUser::getUserId, SecurityUtils.getUserId())
                .eq(PostUser::getStatus, FAVORITE_STATUS);
        postUserService.getBaseMapper().delete(wrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listFavoritePost(Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<PostUser> wrapper = new LambdaQueryWrapper<>();
        //查询用户收藏的帖子
        wrapper.eq(PostUser::getUserId, SecurityUtils.getUserId())
                .eq(PostUser::getStatus, FAVORITE_STATUS);
        Page<PostUser> postUserPage = new Page<>(pageNum, pageSize);
        postUserService.page(postUserPage, wrapper);
        //查出对应帖子的id
        List<Long> postIds = postUserPage.getRecords().stream()
                .map(PostUser::getPostId)
                .collect(Collectors.toList());

        //分页获取相应的帖子
        if (postIds.isEmpty()) {
            return ResponseResult.okResult(new PageVo(new ArrayList(), 0L));
        }

        List<Post> posts = getBaseMapper().selectBatchIds(postIds);
        List<PostListVo> vos = null;

        try {
            vos = getPostListVo(posts);
            //查询并设置每个帖子贴主的头像及其昵称
            setPosterDetail(vos);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return ResponseResult.okResult(new PageVo(vos, postUserPage.getTotal()));
    }

    private void setPosterDetail(List<PostListVo> vos) {
        List<Long> userIds = vos
                .stream()
                .map(PostListVo::getPosterId)
                .collect(Collectors.toList());
        Map<Long, AuthUserBo> AuthUserBoMap = userFeignClient.getBatchUserByUserIds(userIds).getData();

        //将用户名称及其头像set进vos
        for (PostListVo vo : vos) {
            AuthUserBo posterBo = AuthUserBoMap.get(vo.getPosterId());
            vo.setPosterUsername(posterBo.getUserName());
            vo.setPosterAvatar(posterBo.getAvatar());
        }
    }

    @Override
    public ResponseResult modifyPostStatus(ModifyPostStatusDto modifyPostStatusDto) {
        Post byId = getById(modifyPostStatusDto.getPostId());
        if (Objects.isNull(byId) || !(SecurityUtils.getUserId().equals(byId.getPosterId()))) {
            throw new RuntimeException("操作失败");
        }

        byId.setStatus(modifyPostStatusDto.getStatus());
        updateById(byId);

        if(byId.getDisplayLocation().equals(0)) {
            //将MeiliSearch中对应的doc进行更新
            PostBo postBo = meiliSearchUtils.searchDocumentById(POST_INDEX_UID, byId.getId().toString(), PostBo.class);
            postBo.setStatus(modifyPostStatusDto.getStatus());
            meiliSearchUtils.updateDocumentByIndex(postBo, POST_INDEX_UID);

        }
        return ResponseResult.okResult();
    }


    @Override
    public ResponseResult getMatchRelationByPostId(Long postId) {
        //获取队伍
        Team team = teamService.getBaseMapper()
                .selectOne(new LambdaQueryWrapper<Team>()
                        .eq(Team::getPostId, postId));

        //获取队伍成员
        List<Long> userIds = teamUserService.list(
                        new LambdaQueryWrapper<TeamUser>()
                                .eq(TeamUser::getTeamId, team.getId()))
                .stream()
                .map(TeamUser::getUserId).collect(Collectors.toList());

        //获取用户信息
        Map<Long, AuthUserBo> posterBoMap = userFeignClient.getBatchUserByUserIds(userIds).getData();

        List<MatchedUserVo> matchedUserVos = new ArrayList<>();
        //包装成Vo，并返回
        for (Map.Entry<Long, AuthUserBo> AuthUserBoEntry : posterBoMap.entrySet()) {
            MatchedUserVo vo = BeanCopyUtils.copyBean(AuthUserBoEntry.getValue(), MatchedUserVo.class);
            matchedUserVos.add(vo);
        }

        return ResponseResult.okResult(new GetMatchRelationByPostId(matchedUserVos));
    }

    @Override
    public ResponseResult cancelMatchByPostId(Long postId) {
        //获取队伍
        Team team = teamService.getBaseMapper().selectOne(
                new LambdaQueryWrapper<Team>()
                        .eq(Team::getPostId, postId));

        //将用户踢出指定队伍
        teamUserService.getBaseMapper().delete(new LambdaQueryWrapper<TeamUser>()
                .eq(TeamUser::getTeamId, team.getId())
                .eq(TeamUser::getUserId, SecurityUtils.getUserId()));

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getMeMatchedPost(Integer pageNum, Integer pageSize) {
        Long userId = SecurityUtils.getUserId();
        //分页查找队伍
        LambdaQueryWrapper<TeamUser> wrapper = new LambdaQueryWrapper<TeamUser>().eq(TeamUser::getUserId, userId);
        Page<TeamUser> page = new Page<>(pageNum, pageSize);
        teamUserService.page(page, wrapper);
        //查找队伍
        List<Long> teamIds = page.getRecords().stream().map(TeamUser::getTeamId).collect(Collectors.toList());
        List<Team> teams = teamService.getBaseMapper().selectBatchIds(teamIds);
        //查找帖子
        List<Long> postIds = teams.stream().map(Team::getPostId).collect(Collectors.toList());

        if(postIds.isEmpty()) return ResponseResult.okResult(new PageVo(new ArrayList<>() ,0L));

        List<Post> posts = getBaseMapper().selectBatchIds(postIds);
        List<PostListVo> vos = null;

        try {
            vos = getPostListVo(posts);
            setPosterDetail(vos);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return ResponseResult.okResult(new PageVo(vos, page.getTotal()));
    }

    @Override
    public ResponseResult adminListPost(AdminListPostDto adminListPostDto) {
        SearchPostBo searchPostBo = BeanCopyUtils.copyBean(adminListPostDto, SearchPostBo.class);
        return searchPost(searchPostBo);
    }

    @Override
    @Transactional
    public ResponseResult adminDeletePost(AdminDeletePostDto adminDeletePostDto) {
        deletePostById(adminDeletePostDto.getId());
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getRecommendPost(Integer count) {
        List<Post> posts = null;
        try {
            List<Long> recommendByUserid = recommendDataModel.getRecommendByUserid(20, count, SecurityUtils.getUserId());
            //如果推荐为空则直接返回
            if (recommendByUserid.isEmpty()) return ResponseResult.okResult(new ArrayList<>());
            //数据库查询
            posts = getBaseMapper().selectBatchIds(recommendByUserid);

            //如果推荐为空则直接返回
            if (posts.isEmpty()) return ResponseResult.okResult(new ArrayList<>());

            List<Post> recommendPost = posts.stream().filter(post -> (post.getStatus().equals(POST_STATUS_MATCHING)))
                    .collect(Collectors.toList());

            List<PostListVo> vos = getPostListVo(recommendPost);
            return ResponseResult.okResult(vos);
        } catch (Exception e) {
            throw new RuntimeException("获取推荐失败");
        }
    }


    private List<PostListVo> getPostListVo(List<Post> posts) throws ExecutionException, InterruptedException {
        List<Long> postIds = posts.stream().map(Post::getId).collect(Collectors.toList());
        List<Long> posterIds = posts.stream().map(Post::getPosterId).collect(Collectors.toList());
        //异步优化查询POST和TAG关系
        CompletableFuture<List<PostTag>> postTagFuture = CompletableFuture.supplyAsync(() -> {
            return postTagMapper.getPostTagsByPostIds(postIds);
        });

        //异步优化POST和USER关系
        CompletableFuture<Map<Long, AuthUserBo>> postUserFuture = CompletableFuture.supplyAsync(() -> {
            return userFeignClient.getBatchUserByUserIds(posterIds).getData();
        });

        List<PostTag> postTagList = postTagFuture.get();
        Map<Long, AuthUserBo> posterBoMap = postUserFuture.get();

        Map<Long, Long> postTagMap = postTagList.stream().collect(Collectors.toMap(PostTag::getPostId, PostTag::getTagId));
        //查询TAG
        Map<Long, TagBo> tagMap = new HashMap<>();
        List<Long> tagIds = postTagList.stream().map(PostTag::getTagId).collect(Collectors.toList());

        if(!tagIds.isEmpty()) {
            List<Tag> tags = tagService.getBaseMapper().selectBatchIds(tagIds);
            for (Tag tag : tags) {
                TagBo tagBo = BeanCopyUtils.copyBean(tag, TagBo.class);
                tagMap.put(tag.getId(), tagBo);
            }
        }

        //封装VO
        List<PostListVo> vos = BeanCopyUtils.copyBeanList(posts, PostListVo.class);

        for (PostListVo vo : vos) {
            AuthUserBo posterBo = posterBoMap.get(vo.getPosterId());
            //获取TAG_ID
            Long tagID = postTagMap.get(vo.getId());
            //获取TAG
            TagBo tagBo = tagMap.get(tagID);
            vo.setContent(OmitContentKeepPic(vo.getContent()));
            vo.setTags(Collections.singletonList(tagBo));
            vo.setPosterUsername(posterBo.getUserName());
            vo.setPosterAvatar(posterBo.getAvatar());
        }

        return vos;
    }

    private PostShowVo getPostShowVo(Post post) throws ExecutionException, InterruptedException {
        //异步优化查询POST和TAG关系
        CompletableFuture<PostTag> postTagFuture = CompletableFuture.supplyAsync(() -> {
            return postTagService.lambdaQuery().eq(PostTag::getPostId ,post.getId()).one();
        });

        //异步优化POST和USER关系
        CompletableFuture<AuthUserBo> userFuture = CompletableFuture.supplyAsync(() -> {
            return userFeignClient.getUserById(post.getPosterId()).getData();
        });

        PostTag postTag = postTagFuture.get();
        AuthUserBo userBo = userFuture.get();

        //查询TAG
        Tag tag = tagService.getById(postTag.getTagId());

        //封装VO
        PostShowVo vo = BeanCopyUtils.copyBean(post, PostShowVo.class);
        vo.setPosterUsername(userBo.getUserName());
        vo.setPosterAvatar(userBo.getAvatar());

        List<TagBo> tags = new ArrayList<>();

        if(tag != null) {
            tags.add(new TagBo(tag.getId(), tag.getName()));
        }

        vo.setTags(tags);
        return vo;
    }

    @Override
    public ResponseResult editLivePostToGroup(EditLivePostDto dto) {
        Long userId = SecurityUtils.getUserId();
        //判断是否为社群人员
        GroupUser groupUser = groupUserService.getBaseMapper().selectOne(
                new LambdaQueryWrapper<GroupUser>()
                        .eq(GroupUser::getGroupId, dto.getGroupId())
                        .eq(GroupUser::getUserId, userId));

        Optional.ofNullable(groupUser).orElseThrow(() -> new RuntimeException("非该社群人员"));

        //判断是否为打卡
        if(dto.getIsSignin().equals(1)){
            signin(dto.getGroupId() ,userId ,dto.getTeamId());
        }

        Post post = Post.builder()
                .title(dto.getTitle())
                .posterId(userId)
                .content(dto.getContent())
                .type(POST_TYPE_LIVE)
                .status(-1)
                .displayLocation(POST_DISPLAY_GROUP)
                .build();
        //将动态保存
        save(post);

        //保存动态与社群的关系
        postGroupService.save(new PostGroup(dto.getGroupId() ,post.getId()));

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listLiveByGroupId(ListLiveByGroupIdDto dto) {
        //分页器
        Page<PostGroup> postGroupPage = new Page<>(dto.getPageNum() ,dto.getPageSize());
        List<Long> postIds = postGroupService.lambdaQuery()
                .eq(PostGroup::getGroupId, dto.getGroupId())
                .page(postGroupPage)
                .getRecords()
                .stream()
                .map(PostGroup::getPostId)
                .collect(Collectors.toList());

        if(postIds.isEmpty()) return ResponseResult.okResult(new PageVo(new ArrayList<>(),0L));

        List<Post> posts = getBaseMapper().selectBatchIds(postIds);
        List<Long> userIds = posts.stream().map(Post::getPosterId).collect(Collectors.toList());
        Map<Long, AuthUserBo>AuthUserBoMap = userFeignClient.getBatchUserByUserIds(userIds).getData();

        List<ListLiveByGroupIdVo> vos = new ArrayList<>();
        for (Post post : posts) {
            AuthUserBo authUserBo = AuthUserBoMap.get(post.getPosterId());
            vos.add(ListLiveByGroupIdVo.builder()
                            .id(post.getId())
                            .postedId(post.getPosterId())
                            .posterUsername(authUserBo.getUserName())
                            .posterAvatar(authUserBo.getAvatar())
                            .title(post.getTitle())
                            .content(post.getContent())
                            .time(post.getUpdateTime())
                    .build());
        }
        return ResponseResult.okResult(new PageVo(vos , (long) vos.size()));
    }

    @Override
    public ResponseResult getLive(Long id) {
        Post byId = getById(id);
        Long posterId = byId.getPosterId();
        AuthUserBo userBo = userFeignClient.getUserById(posterId).getData();
        GetLiveDetailVo vo = GetLiveDetailVo.builder()
                .id(byId.getId())
                .postedId(userBo.getId())
                .posterUsername(userBo.getUserName())
                .posterAvatar(userBo.getAvatar())
                .title(byId.getTitle())
                .content(byId.getContent())
                .time(byId.getUpdateTime())
                .build();
        return ResponseResult.okResult(vo);
    }

    @Override
    @Transactional
    public ResponseResult addMatchPostToGroup(AddMatchPostToGroupDto dto) {
        Post post = BeanCopyUtils.copyBean(dto, Post.class);
        List<TagBo> tags = dto.getTags();
        //添加帖子进数据库
        saveMatchPostToDb(tags ,post ,1);
        //将帖子与社群绑定
        postGroupService.save(new PostGroup(dto.getGroupId() ,post.getId()));
        Team team = Team.builder()
                .id(SnowFlakeUtil.getDefaultSnowFlakeId())
                .postId(post.getId())
                .build();
        //同时生成队伍
        teamService.save(team);
        //同时生成队伍与社群的关联关系
        groupTeamService.save(GroupTeam.builder()
                .groupId(dto.getGroupId())
                .teamId(team.getId())
                .build());
        return ResponseResult.okResult();
    }


    private void signin(Long groupId ,Long userId ,Long teamId){
        //检查用户是否在队伍中
        TeamUser selectOne = teamUserService.getBaseMapper().selectOne(new LambdaQueryWrapper<TeamUser>()
                .eq(TeamUser::getTeamId, teamId)
                .eq(TeamUser::getUserId, userId));
        Optional.ofNullable(selectOne).orElseThrow(() -> new RuntimeException("用户不存在在此队伍中"));

        //查找今天是否已经打卡
        UserSignin signin = userSigninService.getBaseMapper()
                .selectOne(new QueryWrapper<UserSignin>()
                        .eq("user_id", userId)
                        .eq("team_id", teamId)
                        .orderByAsc("id")
                        .last("limit 1"));

        long currentTime = System.currentTimeMillis();
        if(Objects.isNull(signin) || !TimeUtils.isSameDay(signin.getTime().getTime() ,currentTime)){
            //进行签到记录
            userSigninService.save(UserSignin.builder()
                            .id(SnowFlakeUtil.getDefaultSnowFlakeId())
                            .teamId(teamId)
                            .groupId(groupId)
                            .time(new Date(currentTime))
                            .userId(userId)
                    .build());
        }

    }



    private void deletePostById(Long id) {
        //删除帖子
        LambdaUpdateWrapper<Post> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Post::getId, id)
                .set(Post::getDelFlag, POST_DELETE);

        update(wrapper);

        //同时删除队伍
        Team team = teamService.getBaseMapper().selectOne(new LambdaQueryWrapper<Team>().eq(Team::getPostId, id));
        team.setDelFlag(1);
        teamService.updateById(team);

        //删除相应的document
        meiliSearchUtils.deleteDocumentByIndex("post", id.toString());
    }


    private MeiliSearchUtils.SearchDocumentBo<PostBo> postdocByCondArrToList(String[][] condition, Integer pageSize, Integer pageNum, String q) {
        //筛选出满足条件的doc，根据id排序
        SearchRequest searchRequest = SearchRequest.builder()
                .q(q)
                .filterArray(condition)
                .sort(new String[]{"id:desc"})
                .limit(pageSize)
                .offset((pageNum - 1) * pageSize)
                .build();

        return meiliSearchUtils.searchDocumentArrByIndex(POST_INDEX_UID, searchRequest, PostBo.class);
    }

    /**
     * 截取文章部分内容并且不省略图片链接
     */
    private String OmitContentKeepPic(@NonNull String content) {
        String[] split = content.split("\\*\\*/img/\\*\\*");
        if (split.length == 1) {
            return omitContent(split[0]);
        } else if (split.length == 0) {
            return "";
        }
        String s = omitContent(split[0]);
        return s + "**/img/**" + split[1];
    }

    //截取文章部分内容
    private String omitContent(@NonNull String content) {
        return content.substring(0,
                Math.min(content.length(), LIST_CONTENT_CORP_LENGTH)) + "...";
    }

    //搜索帖子
    private ResponseResult searchPost(SearchPostBo searchPostBo) {
        //获取筛选条件
        Long tagId = searchPostBo.getTagId();
        String q = searchPostBo.getQ();
        Date beginTime = searchPostBo.getBeginTime();
        Date endTime = searchPostBo.getEndTime();
        Integer status = searchPostBo.getStatus();

        //设置筛选条件
        List<String[]> con = new ArrayList<>();
        if (Objects.nonNull(status)) {
            con.add(new String[]{"status = " + status});
        }
        if (Objects.nonNull(tagId) && tagId > 0) {
            con.add(new String[]{"tags.id = " + tagId});
        }
        if (Objects.nonNull(beginTime) && Objects.nonNull(endTime)) {
            con.add(new String[]{"beginTimeStamp >= " + beginTime.getTime() +
                    " AND " + "endTimeStamp <= " + endTime.getTime()});
        }

        //通过MeiliSearch查找
        MeiliSearchUtils.SearchDocumentBo<PostBo> post =
                postdocByCondArrToList(con.toArray(new String[0][]), searchPostBo.getPageSize(), searchPostBo.getPageNum(), q);

        //转化vos
        List<PostListVo> vos = BeanCopyUtils.copyBeanList(post.getData(), PostListVo.class);

        //截取文字，但保留图片
        vos.forEach(o -> o.setContent(OmitContentKeepPic(o.getContent())));

        //查询每个帖子贴主的头像及其昵称
        setPosterDetail(vos);

        return ResponseResult.okResult(new PageVo(vos, (long) post.getTotal()));
    }

    private void saveMatchPostToDb(List<TagBo> tags, Post post , Integer display){
        post.setPosterId(SecurityUtils.getUserId());
        post.setDisplayLocation(display);
        post.setType(0);
        post.setStatus(POST_STATUS_MATCHING);
        //截取文章，将图片不纳入审核
        String content = post.getContent();
        String[] split = content.split("\\*\\*/img/\\*\\*");
        //对帖子标题和内容进行审查屏蔽
        content = SensitiveWordHelper.replace(split[0], '*');
        post.setContent(SensitiveWordHelper.replace(content, '*'));
        //将屏蔽后的图片重新拼接
        if (split.length > 1) {
            content += "**/img/**";
            content += split[1];
        }
        //保存帖子内容
        post.setContent(content);
        save(post);
        //保存标签与帖子得对应关系
        List<PostTag> postTags = new ArrayList<>();
        for (TagBo tag : tags) {
            postTags.add(new PostTag(post.getId(), tag.getId()));
        }
        postTagService.saveBatch(postTags);
    }

}

