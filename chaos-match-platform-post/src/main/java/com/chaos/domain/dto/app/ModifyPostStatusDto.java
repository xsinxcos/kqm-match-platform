package com.chaos.domain.dto.app;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @description: 修改帖子状态
 * @author: xsinxcos
 * @create: 2024-02-12 01:22
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifyPostStatusDto {
    @NotNull(message = "postID不能为空")
    private Long postId;

    @NotNull(message = "status不能为空")
    private Integer status;
}
