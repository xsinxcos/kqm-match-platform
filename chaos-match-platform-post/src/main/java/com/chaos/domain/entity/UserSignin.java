package com.chaos.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
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
    //创建者
    private Long createBy;
    //创建时间
    private Date createTime;
    //更新人
    private Long updateBy;
    //更新者
    private Date updateTime;
    //伪删除标志（0为未删除，1为删除）
    private Integer delFlag;


}

