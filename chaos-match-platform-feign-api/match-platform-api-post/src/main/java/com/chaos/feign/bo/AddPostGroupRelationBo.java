package com.chaos.feign.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-05-12 21:41
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddPostGroupRelationBo {
    private Long postId;

    private Long groupId;
}
