package com.chaos.tag.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: TagListVo
 * @author: xsinxcos
 * @create: 2024-02-06 02:38
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagListVo {
    private Long id;
    //标签名
    private String name;
    //标签图片
    private String url;
}
