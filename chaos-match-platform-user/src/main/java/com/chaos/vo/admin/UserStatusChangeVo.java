package com.chaos.vo.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-03-16 17:18
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStatusChangeVo {
    private Long uid;
    private Integer status;
}
