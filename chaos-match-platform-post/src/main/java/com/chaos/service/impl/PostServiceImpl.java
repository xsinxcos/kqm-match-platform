package com.chaos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.config.vo.PageVo;
import com.chaos.constant.AppHttpCodeEnum;
import com.chaos.domain.dto.*;
import com.chaos.domain.entity.Post;
import com.chaos.domain.entity.PostTag;
import com.chaos.domain.entity.PostUser;
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
import com.chaos.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    private final UserFeignClient userFeignClient;

    private final PostTagService postTagService;

    private final PostUserService postUserService;

    @Override
    public ResponseResult addPost(AddPostDto addPostDto) {
        Post post = BeanCopyUtils.copyBean(addPostDto, Post.class);
        post.setUserId(SecurityUtils.getUserId());

        //保存帖子内容
        save(post);
        //保存标签与帖子得对应关系
        List<PostTag> postTags = new ArrayList<>();
        for (Long tag : addPostDto.getTags()) {
            postTags.add(new PostTag(post.getId(), tag));
        }
        postTagService.saveBatch(postTags);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listPost(Integer pageNum, Integer pageSize, Long tagId) {
        LambdaQueryWrapper<PostTag> wrapper = new LambdaQueryWrapper<>();
        //如果tagId存在则查询
        wrapper.eq(Objects.nonNull(tagId) && tagId > 0, PostTag::getTagId, tagId);
        List<Long> postIds = postTagService.list(wrapper)
                .stream()
                .map(PostTag::getPostId)
                .collect(Collectors.toList());
        //分页获取相应帖子
        if (postIds.isEmpty()) return ResponseResult.okResult(new PageVo(new ArrayList(), 0L));

        QueryWrapper<Post> postQueryWrapper = new QueryWrapper<Post>()
                .in("id", postIds)
                .in("status", POST_STATUS_MATCHING)
                .orderByDesc("update_time");
        Page<Post> postPage = new Page<>(pageNum, pageSize);
        page(postPage, postQueryWrapper);

        List<PostListVo> vos = getAndSetPostListVoByPostPage(postPage);

        return ResponseResult.okResult(new PageVo(vos, postPage.getTotal()));
    }

    @Override
    public ResponseResult showPost(Long postId) {
        //获取帖子
        Post byId = getById(postId);
        //获取发帖人信息
        AuthUserBo authUserBo = userFeignClient.getUserById(byId.getUserId()).getData();
        //获取相关的tag
        LambdaQueryWrapper<PostTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PostTag::getPostId, postId);
        List<Long> tags = postTagService.list(wrapper)
                .stream()
                .map(PostTag::getTagId)
                .collect(Collectors.toList());
        //封装VO
        PostShowVo postShowVo = PostShowVo.builder()
                .id(byId.getId())
                .content(byId.getContent())
                .longitude(byId.getLongitude())
                .latitude(byId.getLatitude())
                .meetAddress(byId.getMeetAddress())
                .title(byId.getTitle())
                .status(byId.getStatus())
                .posterId(byId.getUserId())
                .posterUsername(authUserBo.getUserName())
                .posterAvatar(authUserBo.getAvatar())
                .build();

        return ResponseResult.okResult(postShowVo);
    }

    @Override
    public ResponseResult getMyPost(Integer pageNum, Integer pageSize) {
        //获取用户的userid
        Long userId = SecurityUtils.getUserId();
        //筛选出用户的帖子
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Post::getUserId, userId)
                .orderByDesc(Post::getUpdateTime);
        Page<Post> postPage = new Page<>(pageNum, pageSize);
        page(postPage, wrapper);
        //获取发帖人信息
        AuthUserBo authUserBo = userFeignClient.getUserById(userId).getData();
        //将帖子与贴主进行对应并封装到vo
        List<PostListVo> vos = new ArrayList<>();
        for (Post record : postPage.getRecords()) {
            PostListVo postListVo = PostListVo.builder()
                    .id(record.getId())
                    .title(record.getTitle())
                    .posterId(record.getUserId())
                    .posterAvatar(authUserBo.getAvatar())
                    .status(record.getStatus())
                    .posterUsername(authUserBo.getUserName())
                    .build();
            vos.add(postListVo);
        }

        return ResponseResult.okResult(new PageVo(vos, postPage.getTotal()));
    }

    @Override
    public ResponseResult modifyMyPost(ModifyMyPostDto modifyMyPostDto) {
        Post byId = getById(modifyMyPostDto.getId());
        //检验帖子是否属于该用户
        if (!byId.getUserId().equals(SecurityUtils.getUserId()))
            return ResponseResult.errorResult(AppHttpCodeEnum.ERROR);

        //更新帖子信息
        byId.setTitle(modifyMyPostDto.getTitle());
        byId.setContent(modifyMyPostDto.getContent());
        byId.setLatitude(modifyMyPostDto.getLatitude());
        byId.setLongitude(modifyMyPostDto.getLongitude());
        byId.setMeetAddress(modifyMyPostDto.getMeetAddress());

        updateById(byId);

        //删除原有tag与帖子的关系
        postTagService.getBaseMapper().delete(
                new QueryWrapper<PostTag>().eq("post_id", byId.getId())
        );

        //重新添加tag与帖子的关系
        List<PostTag> postTags = new ArrayList<>();
        for (Long tag : modifyMyPostDto.getTags()) {
            postTags.add(new PostTag(byId.getId(), tag));
        }
        postTagService.saveBatch(postTags);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteMyPost(String id) {
        Post byId = getById(id);
        if (Objects.isNull(byId) || !byId.getUserId().equals(SecurityUtils.getUserId()))
            throw new RuntimeException(AppHttpCodeEnum.NO_OPERATOR_AUTH.getMsg());
        byId.setDelFlag(1);
        updateById(byId);
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
        List<Long> postIds = postUserService.list(wrapper)
                .stream()
                .map(PostUser::getPostId)
                .collect(Collectors.toList());
        //分页获取相应的帖子
        if (postIds.isEmpty()) return ResponseResult.okResult(new PageVo(new ArrayList(), 0L));

        QueryWrapper<Post> postQueryWrapper = new QueryWrapper<Post>()
                .in("id", postIds)
                .orderByDesc("update_time");
        Page<Post> postPage = new Page<>(pageNum, pageSize);
        page(postPage, postQueryWrapper);
        List<PostListVo> vos = getAndSetPostListVoByPostPage(postPage);

        return ResponseResult.okResult(new PageVo(vos, postPage.getTotal()));
    }

    @Override
    public ResponseResult modifyPostStatus(ModifyPostStatusDto modifyPostStatusDto) {
        Post byId = getById(modifyPostStatusDto.getPostId());
        if (Objects.isNull(byId) || !(SecurityUtils.getUserId().equals(byId.getUserId())))
            throw new RuntimeException("操作失败");

        byId.setStatus(modifyPostStatusDto.getStatus());
        updateById(byId);
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

    private List<PostListVo> getAndSetPostListVoByPostPage(Page<Post> postPage) {
        //查询每个帖子贴主的头像及其昵称
        List<Long> postIds = postPage.getRecords()
                .stream()
                .map(Post::getUserId)
                .collect(Collectors.toList());
        Map<Long, PosterBo> posterBoMap = userFeignClient.getBatchUserByUserIds(postIds).getData();

        //将帖子与贴主进行对应并封装到vo
        List<PostListVo> vos = new ArrayList<>();
        for (Post record : postPage.getRecords()) {
            PosterBo posterBo = posterBoMap.get(record.getUserId());
            PostListVo postListVo = PostListVo.builder()
                    .id(record.getId())
                    .title(record.getTitle())
                    .posterId(record.getUserId())
                    .posterAvatar(posterBo.getAvatar())
                    .status(record.getStatus())
                    .posterUsername(posterBo.getUsername())
                    .build();
            vos.add(postListVo);
        }

        return vos;
    }


}

