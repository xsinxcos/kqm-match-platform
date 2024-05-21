package com.chaos.post.domain.vo;

import com.chaos.tag.domain.bo.TagBo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @description: PostShowVo
 * @author: xsinxcos
 * @create: 2024-02-06 05:31
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostShowVo {
    private Long id;
    //贴主ID
    private Long posterId;
    //贴主用户名
    private String posterUsername;
    //贴主头像
    private String posterAvatar;
    //贴子标题
    private String title;
    //帖子内容
    private String content;
    //集合地点
    private String meetAddress;
    //纬度
    private String latitude;
    //经度
    private String longitude;
    //开始时间
    private Date beginTime;
    //结束时间
    private Date endTime;
    //状态（0待匹配，1匹配完成）
    private Integer status;
    //帖子的标签
    private List<TagBo> tags;
    //用户是否收藏
    private Boolean isKeep;
}