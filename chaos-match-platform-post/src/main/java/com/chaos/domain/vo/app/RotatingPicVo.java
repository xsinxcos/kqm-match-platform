package com.chaos.domain.vo.app;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-03-18 21:59
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RotatingPicVo {
    //主键ID@TableId
    private Long id;

    //资源名称
    private String name;
    //资源链接
    private String url;
}
