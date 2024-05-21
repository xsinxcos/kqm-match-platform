package com.chaos.team.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * (Team)表实体类
 *
 * @author chaos
 * @since 2024-05-14 23:37:48
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_team")
public class Team {
    @TableId(type = IdType.INPUT)
    private Long id;

    //队伍名称
    private String name;
    //队伍绑定的帖子的id
    private Long postId;
    //队伍头像
    private String icon;
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
    //伪删除标志
    private Integer delFlag;


}

