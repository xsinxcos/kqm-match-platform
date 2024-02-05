package com.chaos.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @description: ModifyMyPostDto
 * @author: xsinxcos
 * @create: 2024-02-06 06:46
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifyMyPostDto {
    private Long id;
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
    @NonNull
    private Long[] tags;
}
