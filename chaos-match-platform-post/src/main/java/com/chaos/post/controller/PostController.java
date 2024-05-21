package com.chaos.post.controller;

import com.chaos.annotation.SystemLog;
import com.chaos.common.util.GroupMemberUtils;
import com.chaos.post.domain.dto.*;
import com.chaos.post.domain.entity.Post;
import com.chaos.post.domain.entity.PostGroup;
import com.chaos.post.service.PostGroupService;
import com.chaos.post.service.PostService;
import com.chaos.response.ResponseResult;
import com.chaos.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * @description: PostController
 * @author: xsinxcos
 * @create: 2024-02-02 05:23
 **/

/**
 * 帖子模块（APP端）
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    private final PostGroupService postGroupService;

    private final GroupMemberUtils groupMemberUtils;

    /**
     * 发布帖子
     *
     * @param addPostDto
     * @return
     */
    @PostMapping("/edit")
    @SystemLog(BusinessName = "addPost")
    public ResponseResult addPost(@RequestBody @Valid AddPostDto addPostDto) {
        return postService.addPost(addPostDto);
    }


    /**
     * 罗列条件帖子
     *
     * @param listPostDto listPostDto
     * @return
     */
    @PostMapping("/list")
    @SystemLog(BusinessName = "listPost")
    public ResponseResult listPost(@RequestBody @Valid ListPostDto listPostDto) {
        return postService.listPost(listPostDto);
    }

    /**
     * 展示帖子
     *
     * @param id
     * @return
     */
    @GetMapping("/list/{id}")
    @SystemLog(BusinessName = "showPost")
    public ResponseResult showPost(@PathVariable @NotNull Long id) {
        return postService.showPost(id);
    }

    /**
     * 罗列本人帖子
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/getme")
    @SystemLog(BusinessName = "getMyPost")
    public ResponseResult getMyPost(@NotNull Integer pageNum, @NotNull Integer pageSize) {
        return postService.getMyPost(pageNum, pageSize);
    }

    /**
     * 修改本人帖子
     *
     * @param modifyMyPostDto
     * @return
     */
    @PutMapping("/getme")
    @SystemLog(BusinessName = "modifyMyPost")
    public ResponseResult modifyMyPost(@RequestBody @Valid ModifyMyPostDto modifyMyPostDto) {
        return postService.modifyMyPost(modifyMyPostDto);
    }

    /**
     * 删除本人帖子
     *
     * @param id 帖子ID
     * @return
     */
    @DeleteMapping("/getme/{id}")
    @SystemLog(BusinessName = "deleteMyPost")
    public ResponseResult deleteMyPost(@PathVariable @NotNull Long id) {
        return postService.deleteMyPost(id);
    }

    /**
     * 收藏帖子
     *
     * @param addFavoritePostDto
     * @return
     */
    @PostMapping("/favorite")
    @SystemLog(BusinessName = "addFavoritePost")
    public ResponseResult addFavoritePost(@RequestBody @Valid AddFavoritePostDto addFavoritePostDto) {
        return postService.addFavoritePost(addFavoritePostDto);
    }

    /**
     * 移除收藏的帖子
     *
     * @param dto
     * @return
     */

    @DeleteMapping("/favorite")
    @SystemLog(BusinessName = "deleteFavoritePost")
    public ResponseResult deleteFavoritePost(@RequestBody @Valid DeleteFavoritePostDto dto) {
        Post byId = postService.getById(dto.getId());
        Optional.ofNullable(byId).orElseThrow(() -> (new RuntimeException("操作失败")));
        return postService.deleteFavoritePost(dto);
    }

    /**
     * 罗列收藏的帖子
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/favorite")
    @SystemLog(BusinessName = "listFavoritePost")
    public ResponseResult listFavoritePost(@NotNull Integer pageNum, @NotNull Integer pageSize) {
        return postService.listFavoritePost(pageNum, pageSize);
    }

    /**
     * 修改帖子状态
     *
     * @param modifyPostStatusDto
     * @return
     */
    @PostMapping("/modify")
    @SystemLog(BusinessName = "modifyPostStatus")
    public ResponseResult modifyPostStatus(@RequestBody @Valid ModifyPostStatusDto modifyPostStatusDto) {
        return postService.modifyPostStatus(modifyPostStatusDto);
    }

    /**
     * 获取帖子所对应的匹配用户
     *
     * @param postId
     * @return
     */

    @GetMapping("/matched")
    @SystemLog(BusinessName = "getMatchRelationByPostId")
    public ResponseResult getMatchRelationByPostId(@NotNull Long postId) {
        return postService.getMatchRelationByPostId(postId);
    }


    /**
     * 用户主动取消匹配状态
     *
     * @param postId
     * @return
     */

    @DeleteMapping("/matched/cancel/{postId}")
    @SystemLog(BusinessName = "cancelMatchByPostId")
    public ResponseResult cancelMatchByPostId(@PathVariable @NotNull Long postId) {
        return postService.cancelMatchByPostId(postId);
    }

    /**
     * 用于获取本人的匹配历史
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/getme/matched")
    @SystemLog(BusinessName = "getMeMatchedPost")
    public ResponseResult getMeMatchedPost(@NotNull Integer pageNum, @NotNull Integer pageSize) {
        return postService.getMeMatchedPost(pageNum, pageSize);
    }

    /**
     * 获取推荐帖子
     *
     * @return
     */
    @GetMapping("/recommend")
    @SystemLog(BusinessName = "getRecommendPost")
    public ResponseResult getRecommendPost(@NotNull Integer count) {
        return postService.getRecommendPost(count);
    }

    /**
     * 发布动态到社群中
     * @param dto
     * @return
     */
    @PostMapping("/edit/live")
    @SystemLog(BusinessName = "editLivePostToGroup")
    public ResponseResult editLivePostToGroup(@RequestBody @Valid EditLivePostDto dto){
        boolean isGroupMember = groupMemberUtils.isGroupMember(SecurityUtils.getUserId(), dto.getGroupId());
        if(!isGroupMember) throw new RuntimeException("无权限");
        return postService.editLivePostToGroup(dto);
    }

    /**
     * 罗列动态（社群中）
     * @param dto
     * @return
     */
    @PostMapping("/list/live")
    @SystemLog(BusinessName = "listLiveByGroupId")
    public ResponseResult listLiveByGroupId(@RequestBody @Valid ListLiveByGroupIdDto dto){
        return postService.listLiveByGroupId(dto);
    }

    /**
     * 展示动态（社群中）
     * @param id
     * @return
     */
    @GetMapping("/list/live/{id}")
    @SystemLog(BusinessName = "getLive")
    public ResponseResult getLive(@PathVariable Long id){
        return postService.getLive(id);
    }

    /**
     * 社群中发布匹配帖子
     * @param dto
     * @return
     */
    @PostMapping("/group/edit")
    @SystemLog(BusinessName = "addMatchPostToGroup")
    public ResponseResult editMatchPostToGroup(@RequestBody @Valid AddMatchPostToGroupDto dto){
        Long userId = SecurityUtils.getUserId();
        boolean isGroupMember = groupMemberUtils.isGroupMember(userId, dto.getGroupId());
        if(!isGroupMember) throw new RuntimeException("无权限");
        return postService.addMatchPostToGroup(dto);
    }
}
