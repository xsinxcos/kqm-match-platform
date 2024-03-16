package com.chaos.domain.dto.app;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 修改帖子状态
 * @author: xsinxcos
 * @create: 2024-02-12 01:22
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifyPostStatusDto {
    private Long postId;
    private Integer status;
}
