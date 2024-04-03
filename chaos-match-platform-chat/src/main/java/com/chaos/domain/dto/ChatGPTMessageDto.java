package com.chaos.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-02-05 15:27
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatGPTMessageDto {

    @NotBlank(message = "问题不能为空")
    private String question;
}
