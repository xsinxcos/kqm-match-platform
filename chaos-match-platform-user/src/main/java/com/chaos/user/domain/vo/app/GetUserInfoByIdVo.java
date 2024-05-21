package com.chaos.user.domain.vo.app;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-05-15 01:18
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetUserInfoByIdVo {
    private String userName;
    private Integer sex;
    private String avatar;
    private String selfLabel;
}
