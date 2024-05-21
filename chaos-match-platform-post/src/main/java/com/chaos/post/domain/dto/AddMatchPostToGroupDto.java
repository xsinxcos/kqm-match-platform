package com.chaos.post.domain.dto;

import com.chaos.tag.domain.bo.TagBo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * @author : wzq
 * @since : 2024-05-19 15:09
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddMatchPostToGroupDto {
    //社群ID
    private Long groupId;
    //标题
    @NotBlank(message = "标题不能为空")
    private String title;
    //帖子内容
    @NotBlank(message = "内容不能为空")
    private String content;
    //集合地点
    private String meetAddress;
    //开始时间
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date beginTime;
    //结束时间
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date endTime;
    //纬度
    private String latitude;
    //经度
    private String longitude;
    //标签
    private List<TagBo> tags;
}
