package com.chaos.group.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-05-12 01:22
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupOwnerBo {
    private Long uid;

    private String username;

    private Integer sex;

    private String avatar;

    private String selfLabel;
}
