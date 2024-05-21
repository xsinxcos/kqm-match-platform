package com.chaos.staticResource.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户端静态资源(Resource)表实体类
 *
 * @author chaos
 * @since 2024-03-18 21:30:54
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_resource")
public class Resource {
    //主键ID@TableId
    private Long id;

    //资源名称
    private String name;
    //资源链接
    private String url;
    //资源类型（0为轮播图资源）
    private Integer type;
    //创建者
    private Long createBy;
    //创建时间
    private Date createTime;
    //更新者
    private Integer updateBy;
    //更新时间
    private Date updateTime;
    //删除标志（0为未删除，1为删除）
    private Integer delFlag;


}

