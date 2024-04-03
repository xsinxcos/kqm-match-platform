package com.chaos.domain.dto.app;

import com.chaos.domain.bo.TagBo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @description: ModifyMyPostDto
 * @author: xsinxcos
 * @create: 2024-02-06 06:46
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifyMyPostDto {
    @NotNull(message = "id不能为null")
    private Long id;
    //标题
    @NotBlank(message = "标题不能为空")
    private String title;
    //帖子内容
    @NotBlank(message = "内容不能为空")
    private String content;
    //集合地点
    private String meetAddress;
    //纬度
    private String latitude;
    //经度
    private String longitude;
    //开始时间
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date beginTime;
    //结束时间
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date endTime;
    //标签
    @NonNull
    private List<TagBo> tags;
}
