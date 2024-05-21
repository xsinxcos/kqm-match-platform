package com.chaos.group.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 社群与用户关系表(GroupUser)表实体类
 *
 * @author chaos
 * @since 2024-05-09 21:23:45
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_group_user")
public class GroupUser {
    //用户ID@TableId
    private Long userId;
    //社群ID@TableId
    private Long groupId;

    //0为普通用户 1为管理员 2为超级管理员
    private Integer type;
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


}

