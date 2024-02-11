package com.chaos.feign.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description: modifyPostStatusBo
 * @author: xsinxcos
 * @create: 2024-02-12 01:43
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddPostUserMatchRelationBo {
    private Long postId;
    private List<Long> userIds;
}
