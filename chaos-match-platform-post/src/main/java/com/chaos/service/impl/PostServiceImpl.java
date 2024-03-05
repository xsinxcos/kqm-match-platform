package com.chaos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.config.vo.PageVo;
import com.chaos.constant.AppHttpCodeEnum;
import com.chaos.domain.bo.PostBo;
import com.chaos.domain.bo.TagBo;
import com.chaos.domain.dto.*;
import com.chaos.domain.entity.Post;
import com.chaos.domain.entity.PostTag;
import com.chaos.domain.entity.PostUser;
import com.chaos.domain.vo.GetMatchRelationByPostId;
import com.chaos.domain.vo.MatchedUserVo;
import com.chaos.domain.vo.PostListVo;
import com.chaos.domain.vo.PostShowVo;
import com.chaos.feign.UserFeignClient;
import com.chaos.feign.bo.AddPostUserMatchRelationBo;
import com.chaos.feign.bo.AuthUserBo;
import com.chaos.feign.bo.PosterBo;
import com.chaos.mapper.PostMapper;
import com.chaos.response.ResponseResult;
import com.chaos.service.PostService;
import com.chaos.service.PostTagService;
import com.chaos.service.PostUserService;
import com.chaos.util.BeanCopyUtils;
import com.chaos.util.MeiliSearchUtils;
import com.chaos.util.SecurityUtils;
import com.meilisearch.sdk.SearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.LinkOption;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 帖子表(Post)表服务实现类
 *
 * @author chaos
 * @since 2024-02-01 07:56:43
 */
@Service("postService")
@RequiredArgsConstructor
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {
    private static final int FAVORITE_STATUS = 1;
    private static final int USER_POST_MATCH_STATUS = 0;

    private static final int POST_STATUS_MATCHING = 0;
    private static final int POST_STATUS_MATCH_COMPLETE = 1;
    private static final int POST_STATUS_HAND_UP = 2;
    private static final int POST_DELETE = 1;
    private static final int LIST_CONTENT_CORP_LENGTH = 20;
    private static final String POST_INDEX_UID = "post";

    private final UserFeignClient userFeignClient;

    private final PostTagService postTagService;

    private final PostUserService postUserService;

    private final MeiliSearchUtils meiliSearchUtils;

    @Override
    public ResponseResult addPost(AddPostDto addPostDto) {
        Post post = BeanCopyUtils.copyBean(addPostDto, Post.class);
        post.setPosterId(SecurityUtils.getUserId());
        //保存帖子内容
        save(post);
        //保存标签与帖子得对应关系
        List<PostTag> postTags = new ArrayList<>();
        for (TagBo tag : addPostDto.getTags()) {
            postTags.add(new PostTag(post.getId(), tag.getId()));
        }
        postTagService.saveBatch(postTags);
        //将帖子存入MeiliSearch中
        PostBo postBo = BeanCopyUtils.copyBean(post, PostBo.class);
        postBo.setStatus(POST_STATUS_MATCHING);
        postBo.setTags(addPostDto.getTags());
        postBo.setBeginTimeStamp(postBo.getBeginTime().getTime());
        postBo.setEndTimeStamp(postBo.getEndTime().getTime());

        meiliSearchUtils.addDocumentByIndex(postBo, POST_INDEX_UID);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listPost(ListPostDto listPostDto) {
        //获取筛选条件
        Long tagId = listPostDto.getTagId();
        String q = listPostDto.getQ();
        Date beginTime = listPostDto.getBeginTime();
        Date endTime = listPostDto.getEndTime();

        //设置筛选条件
        List<String[]> con = new ArrayList<>();
        con.add(new String[]{"status = " + POST_STATUS_MATCHING});

        if (Objects.nonNull(tagId) && tagId > 0)
            con.add(new String[]{"tags.id = " + tagId});
        if (Objects.nonNull(beginTime) && Objects.nonNull(endTime))
            con.add(new String[]{"beginTimeStamp >= " + beginTime.getTime() +
                    " AND " + "endTimeStamp <= " + endTime.getTime()});

        //通过MeiliSearch查找
        MeiliSearchUtils.SearchDocumentBo<PostBo> post =
                postdocByCondArrToList(con.toArray(new String[0][]), listPostDto.getPageSize(), listPostDto.getPageNum(), q);

        //转化vos
        List<PostListVo> vos = BeanCopyUtils.copyBeanList(post.getData(), PostListVo.class);

        //查询每个帖子贴主的头像及其昵称
        setPosterDetail(vos);
        return ResponseResult.okResult(new PageVo(vos, (long) post.getTotal()));
    }

    @Override
    public ResponseResult showPost(Long postId) {
        //获通过MeiliSearch帖子
        PostBo byId = meiliSearchUtils.searchDocumentById(POST_INDEX_UID, postId.toString(), PostBo.class);
        //获取发帖人信息
        AuthUserBo authUserBo = userFeignClient.getUserById(byId.getPosterId()).getData();
        //封装VO
        PostShowVo postShowVo = BeanCopyUtils.copyBean(byId, PostShowVo.class);
        postShowVo.setTags(byId.getTags());
        postShowVo.setPosterUsername(authUserBo.getUserName());
        postShowVo.setPosterAvatar(authUserBo.getAvatar());

        return ResponseResult.okResult(postShowVo);
    }

    @Override
    public ResponseResult getMyPost(Integer pageNum, Integer pageSize) {
        //获取用户的userid
        Long userId = SecurityUtils.getUserId();
        //通过MeiliSearch筛选出用户的帖子
        MeiliSearchUtils.SearchDocumentBo<PostBo> documentBo = postdocByCondArrToList(new String[][]{new String[]{"posterId = " + userId}},
                pageSize, pageNum, null);
        //获取发帖人信息
        AuthUserBo authUserBo = userFeignClient.getUserById(userId).getData();
        //将帖子与贴主信息进行对应并封装到vo
        List<PostListVo> vos = BeanCopyUtils.copyBeanList(documentBo.getData(), PostListVo.class);
        vos.forEach(o -> {
            o.setPosterUsername(authUserBo.getUserName());
            o.setPosterAvatar(authUserBo.getAvatar());
        });
        return ResponseResult.okResult(new PageVo(vos, (long) documentBo.getTotal()));
    }

    @Override
    public ResponseResult modifyMyPost(ModifyMyPostDto modifyMyPostDto) {
        Post byId = getById(modifyMyPostDto.getId());
        //检验帖子是否属于该用户
        if (!byId.getPosterId().equals(SecurityUtils.getUserId()))
            return ResponseResult.errorResult(AppHttpCodeEnum.ERROR);

        //更新帖子信息
        byId.setTitle(modifyMyPostDto.getTitle());
        byId.setContent(modifyMyPostDto.getContent());
        byId.setLatitude(modifyMyPostDto.getLatitude());
        byId.setLongitude(modifyMyPostDto.getLongitude());
        byId.setMeetAddress(modifyMyPostDto.getMeetAddress());
        byId.setBeginTime(modifyMyPostDto.getBeginTime());
        byId.setEndTime(modifyMyPostDto.getEndTime());
        updateById(byId);

        //删除原有tag与帖子的关系
        postTagService.getBaseMapper().delete(
                new QueryWrapper<PostTag>().eq("post_id", byId.getId())
        );

        //重新添加tag与帖子的关系
        List<PostTag> postTags = new ArrayList<>();
        for (TagBo tag : modifyMyPostDto.getTags()) {
            postTags.add(new PostTag(byId.getId(), tag.getId()));
        }
        postTagService.saveBatch(postTags);

        //更新meilisearch中的document
        PostBo postBo = BeanCopyUtils.copyBean(byId, PostBo.class);
        postBo.setTags(modifyMyPostDto.getTags());
        postBo.setBeginTimeStamp(postBo.getBeginTime().getTime());
        postBo.setEndTimeStamp(postBo.getEndTime().getTime());

        meiliSearchUtils.updateDocumentByIndex(postBo, POST_INDEX_UID);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteMyPost(String id) {
        //检查帖子所属人
        Post byId = getById(id);
        if (Objects.isNull(byId) || !byId.getPosterId().equals(SecurityUtils.getUserId()))
            throw new RuntimeException(AppHttpCodeEnum.NO_OPERATOR_AUTH.getMsg());
        //删除帖子
        LambdaUpdateWrapper<Post> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Post::getId, id)
                .set(Post::getDelFlag, POST_DELETE);

        update(wrapper);

        //删除相应的document
        meiliSearchUtils.deleteDocumentByIndex("post", byId.getId().toString());
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addFavoritePost(AddFavoritePostDto addFavoritePostDto) {
        Long userId = SecurityUtils.getUserId();
        Long id = addFavoritePostDto.getId();
        Post byId = getById(id);
        if (Objects.isNull(byId)) throw new RuntimeException("帖子不存在");
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
        if (postIds.isEmpty()) return ResponseResult.okResult(new PageVo(new ArrayList(), 0L));
        //通过MeiliSearch查找相应的帖子并封装VO
        List<PostBo> postBos = postIds.stream()
                .map(o -> meiliSearchUtils.searchDocumentById(POST_INDEX_UID, o.toString(), PostBo.class))
                .collect(Collectors.toList());

        List<PostListVo> vos = BeanCopyUtils.copyBeanList(postBos, PostListVo.class);

        //查询并设置每个帖子贴主的头像及其昵称
        setPosterDetail(vos);

        return ResponseResult.okResult(new PageVo(vos, postUserPage.getTotal()));
    }

    private void setPosterDetail(List<PostListVo> vos) {
        List<Long> userIds = vos
                .stream()
                .map(PostListVo::getPosterId)
                .collect(Collectors.toList());
        Map<Long, PosterBo> posterBoMap = userFeignClient.getBatchUserByUserIds(userIds).getData();

        //将用户名称及其头像set进vos
        for (PostListVo vo : vos) {
            PosterBo posterBo = posterBoMap.get(vo.getPosterId());
            vo.setPosterUsername(posterBo.getUserName());
            vo.setPosterAvatar(posterBo.getAvatar());
        }
    }

    @Override
    public ResponseResult modifyPostStatus(ModifyPostStatusDto modifyPostStatusDto) {
        Post byId = getById(modifyPostStatusDto.getPostId());
        if (Objects.isNull(byId) || !(SecurityUtils.getUserId().equals(byId.getPosterId())))
            throw new RuntimeException("操作失败");

        byId.setStatus(modifyPostStatusDto.getStatus());
        updateById(byId);

        //将MeiliSearch中对应的doc进行更新
        PostBo postBo = meiliSearchUtils.searchDocumentById(POST_INDEX_UID, byId.getId().toString(), PostBo.class);
        postBo.setStatus(modifyPostStatusDto.getStatus());
        meiliSearchUtils.updateDocumentByIndex(postBo, POST_INDEX_UID);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addPostUserMatchRelation(AddPostUserMatchRelationBo addPostUserMatchRelationBo) {
        for (Long userId : addPostUserMatchRelationBo.getUserIds()) {
            postUserService.getBaseMapper().insert(new PostUser(
                    addPostUserMatchRelationBo.getPostId(),
                    userId,
                    USER_POST_MATCH_STATUS)
            );
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getMatchRelationByPostId(Long postId) {
        LambdaQueryWrapper<PostUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Objects.nonNull(postId) ,PostUser::getPostId ,postId)
                .eq(PostUser::getStatus ,USER_POST_MATCH_STATUS);
        List<Long> matchedUserIds = postUserService.list(wrapper).stream()
                .map(PostUser::getUserId)
                .collect(Collectors.toList());

        Map<Long, PosterBo> posterBoMap = userFeignClient.getBatchUserByUserIds(matchedUserIds).getData();

        List<MatchedUserVo> matchedUserVos = new ArrayList<>();

        for(Map.Entry<Long ,PosterBo> posterBoEntry : posterBoMap.entrySet()) {
            MatchedUserVo vo = BeanCopyUtils.copyBean(posterBoEntry, MatchedUserVo.class);
            matchedUserVos.add(vo);
        }

        return ResponseResult.okResult(new GetMatchRelationByPostId(matchedUserVos));
    }

    private MeiliSearchUtils.SearchDocumentBo<PostBo> postdocByCondArrToList(String[][] condition, Integer pageSize, Integer pageNum, String q) {
        //筛选出满足条件的doc，根据id排序
        SearchRequest searchRequest = SearchRequest.builder()
                .q(q)
                .filterArray(condition)
                .sort(new String[]{"id:desc"})
                .attributesToCrop(new String[]{"content"})
                .cropLength(LIST_CONTENT_CORP_LENGTH)
                .limit(pageSize)
                .offset((pageNum - 1) * pageSize)
                .build();

        return meiliSearchUtils.searchDocumentArrByIndex(POST_INDEX_UID, searchRequest, PostBo.class);
    }


}

