package com.chaos.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 发送消息表(MessageSend)表实体类
 *
 * @author chaos
 * @since 2024-01-23 05:12:39
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_message_send")
public class MessageSend {
    @TableId
    private Long id;

    //消息内容
    private String content;
    //发送者
    private Long msgFrom;

    private Long msgTo;
    //消息类型
    private Integer type;
    //创建时间
    private Date sendTime;
    //删除标志（0为保留，1为删除）
    private Integer delFlag;


}

