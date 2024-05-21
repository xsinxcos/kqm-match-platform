package com.chaos.post.domain.vo;

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
    private Long id;
    private String userName;
    private String avatar;
}
