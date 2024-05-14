package com.chaos.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 小社区（社群）(Group)表实体类
 *
 * @author chaos
 * @since 2024-05-09 21:09:07
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_group")
public class Group {
    //主键ID@TableId
    @TableId(type = IdType.INPUT)
    private Long id;
    //社区名称
    private String name;
    //社群图标
    private String icon;
    //社群状态
    private Integer status;
    //可见性（0为不可见 ，1为可见）
    private Integer visibility;
    //标签
    private String label;
    //介绍
    private String introduction;
    //社区类型
    private Integer type;
    //类别ID
    private Long categoryId;
    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //创建者
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;
    //更新者
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;
    //删除标志 0为未删除 1为删除
    private Integer delFlag;


}

