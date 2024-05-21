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
 * 队伍与社群的关系表(GroupTeam)表实体类
 *
 * @author chaos
 * @since 2024-05-15 01:55:16
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_group_team")
public class GroupTeam {
    //社群ID@TableId
    private Long groupId;
    //队伍ID@TableId
    private Long teamId;

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
    //伪删除标识
    private Integer delFlag;


}

