package com.chaos.domain.vo;

import com.chaos.domain.bo.TagBo;
import com.chaos.domain.dto.TagDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @description: PostListVo
 * @author: xsinxcos
 * @create: 2024-02-06 03:50
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostListVo {
    private Long id;

    //贴主ID
    private Long posterId;
    //贴主用户名
    private String posterUsername;
    //贴主头像
    private String posterAvatar;
    //贴子标题
    private String title;
    //帖子部分内容
    private String content;
    //开始时间
    private Date beginTime;
    //结束时间
    private Date endTime;
    //状态（0待匹配，1匹配完成）
    private Integer status;
    //帖子的标签
    private List<TagBo> tags;
}
