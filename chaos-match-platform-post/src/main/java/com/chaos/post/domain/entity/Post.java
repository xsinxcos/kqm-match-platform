package com.chaos.post.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 帖子表(Post)表实体类
 *
 * @author chaos
 * @since 2024-02-01 07:56:42
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_post")
public class Post {
    @TableId
    private Long id;

    //贴主ID
    private Long posterId;
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
    //状态（0待匹配，1匹配完成）
    private Integer status;
    //帖子类型（匹配为 0 ，动态为 1）
    private Integer type;
    //可见范围（大厅为 0 ，社群为 1）
    private Integer displayLocation;
    //开始时间
    private Date beginTime;
    //结束时间
    private Date endTime;
    //乐观锁
    @Version
    private Integer version;
    //创建者
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //更新者
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;
    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;


}

