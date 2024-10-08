package com.chaos.post.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @description: ListPostDto
 * @author: xsinxcos
 * @create: 2024-02-21 12:21
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminListPostDto {
    //页码
    @NotNull(message = "pageNum不能为null")
    private Integer pageNum;
    //每页条数
    @NotNull(message = "pageSize不能为null")
    private Integer pageSize;
    //标签ID
    private Long tagId;
    //内容查询
    private String q;
    //查询时间起始
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date beginTime;
    //查询时间结束
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date endTime;
    //状态
    private Integer status;
}
