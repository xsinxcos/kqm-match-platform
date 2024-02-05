package com.chaos.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 发布帖子
 * @author: xsinxcos
 * @create: 2024-02-06 02:29
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddPostDto {
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
    //标签
    private Long[] tags;
}
