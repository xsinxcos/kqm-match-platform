package com.chaos.feign.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 贴主用户名及其头像
 * @author: xsinxcos
 * @create: 2024-02-06 04:12
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PosterBo {
    private Long id;
    private String username;
    private String avatar;
}
