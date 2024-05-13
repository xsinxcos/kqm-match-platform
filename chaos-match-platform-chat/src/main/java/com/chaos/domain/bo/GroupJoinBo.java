package com.chaos.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-05-12 20:42
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupJoinBo {
    private Long from;
    private Long to;
    private Long postId;
    private Long groupId;
}
