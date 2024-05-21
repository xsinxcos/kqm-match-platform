package com.chaos.comment.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @description: DeleteCommentDto
 * @author: xsinxcos
 * @create: 2024-02-16 04:18
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteCommentDto {
    @NotNull(message = "Id不能为NULL")
    private Long commentId;
}
