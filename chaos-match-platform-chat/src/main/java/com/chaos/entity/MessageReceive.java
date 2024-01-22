package com.chaos.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 推送消息表（保存用户收到的消息）(MessageReceive)表实体类
 *
 * @author chaos
 * @since 2024-01-23 05:13:43
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_message_receive")
public class MessageReceive {
    @TableId
    private Long id;

    //发送者
    private Long msgFrom;
    //接收者
    private Long msgTo;
    //消息内容
    private String content;
    //服务端收到消息的时间
    private Date sendTime;
    //消息类型
    private Integer type;
    //接收状态（0：未送达；1：送达）
    private Integer delivered;
    //删除标志（0为正常，1为删除）
    private Integer delFlag;


}

