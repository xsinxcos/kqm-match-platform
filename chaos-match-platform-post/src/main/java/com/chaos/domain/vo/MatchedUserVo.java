package com.chaos.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-03-05 21:03
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchedUserVo {
    private String userName;
    private Integer sex;
    private String avatar;
}