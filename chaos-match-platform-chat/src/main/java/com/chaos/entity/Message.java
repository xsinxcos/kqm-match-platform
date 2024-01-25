package com.chaos.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 消息表(Message)表实体类
 *
 * @author chaos
 * @since 2024-01-25 21:00:19
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_message")
public class Message {
    @TableId
    private Long id;

    //消息内容
    private String content;
    //发送者
    private Long msgFrom;
    //接收者
    private Long msgTo;
    //消息类型
    private Integer type;
    //发送时间
    private Date sendTime;
    //删除标志（0为保留，1为删除）
    private Integer delFlag;


}

