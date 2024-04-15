package com.chaos.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-04-14 19:11
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistoryChatUserVo {
    //用户ID
    private Long id;
    //用户名
    private String userName;
    //头像
    private String avatar;
}
