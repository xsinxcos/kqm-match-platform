package com.chaos.post.domain.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-03-16 22:04
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchPostBo {
    //页码
    private Integer pageNum;
    //每页条数
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
