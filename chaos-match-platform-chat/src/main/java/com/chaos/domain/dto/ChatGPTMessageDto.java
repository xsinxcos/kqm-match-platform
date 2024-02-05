package com.chaos.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-02-05 15:27
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatGPTMessageDto {
    private String question;
}
