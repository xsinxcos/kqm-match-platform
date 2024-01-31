package com.chaos.domain.bo;

import com.chaos.domain.entity.MessageInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 消息
 * @author: xsinxcos
 * @create: 2024-01-23 02:58
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageBo {
    private String type;
    private MessageInfo message;
}
