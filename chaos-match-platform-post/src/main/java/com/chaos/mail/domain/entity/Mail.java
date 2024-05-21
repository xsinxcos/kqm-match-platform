package com.chaos.mail.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 邮件表(Mail)表实体类
 *
 * @author wzq
 * @since 2024-05-18 23:02:12
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_mail")
public class Mail {

    //ID主键
    @TableId(type = IdType.INPUT)
    private Long id;
    //发送人ID（-1 为系统通知）
    private Long senderId;
    //接收人ID
    private Long receiverId;
    //消息类型（JSON格式）
    private String message;
    //是否已读（0未读，1已读，2已读接受，3已读拒绝）
    private Integer isRead;
    //类型
    private Integer type;
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
    //未删除标志（0为未删除，1为删除）
    private Integer delFlag;


}

