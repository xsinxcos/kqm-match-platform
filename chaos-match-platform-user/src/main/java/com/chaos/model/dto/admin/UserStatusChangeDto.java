package com.chaos.model.dto.admin;

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
public class UserStatusChangeDto {
    private Long uid;
    private Integer status;
}
