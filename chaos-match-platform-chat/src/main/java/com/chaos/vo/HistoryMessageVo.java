package com.chaos.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @description: HistoryMessageVo
 * @author: xsinxcos
 * @create: 2024-01-26 03:38
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryMessageVo {
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
}
