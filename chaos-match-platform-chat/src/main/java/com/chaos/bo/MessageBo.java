package com.chaos.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 消息DTO
 * @author: xsinxcos
 * @create: 2024-01-23 02:58
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageBo {
    private Long sendFrom;
    private Long sendTo;
    private String content;
}
