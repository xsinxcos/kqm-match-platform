package com.chaos.post.domain.bo;

import com.chaos.tag.domain.bo.TagBo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-02-19 05:49
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostBo {
    private Long id;

    //贴主ID
    private Long posterId;
    //标题
    private String title;
    //帖子内容
    private String content;
    //集合地点
    private String meetAddress;
    //纬度
    private String latitude;
    //经度
    private String longitude;
    //状态（0待匹配，1匹配完成）
    private Integer status;
    //开始时间
    private Date beginTime;
    //开始时间时间戳
    private Long beginTimeStamp;
    //结束时间
    private Date endTime;
    //结束时间时间戳
    private Long endTimeStamp;
    //拥有标签
    private List<TagBo> tags;
}
