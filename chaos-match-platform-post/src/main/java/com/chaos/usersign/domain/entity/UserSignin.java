package com.chaos.usersign.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户签到表(UserSignin)表实体类
 *
 * @author chaos
 * @since 2024-05-12 02:34:01
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@TableName("t_user_signin")
public class UserSignin {
    //主键ID@TableId
    @TableId(type = IdType.INPUT)
    private Long id;

    //打卡时间
    private Date time;
    //打卡人员
    private Long userId;
    //打卡帖子ID
    private Long postId;
    //打卡队伍ID
    private Long teamId;
    //社群ID
    private Long groupId;
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

