package com.chaos.vo.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-03-16 17:25
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditAccessRightsVo {
    private Long uid;
    private Integer type;
}
