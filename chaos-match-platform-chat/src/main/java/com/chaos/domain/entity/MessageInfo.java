package com.chaos.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: messageInfo
 * @author: xsinxcos
 * @create: 2024-01-24 01:28
 **/
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class MessageInfo {
    private Long uuid;

    private Long sendFrom;

    private Long sendTo;

    private String content;

    private Long groupId;

    private Long postId;

    private Long timestamp;
}
