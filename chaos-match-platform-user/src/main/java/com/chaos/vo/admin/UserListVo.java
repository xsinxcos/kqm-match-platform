package com.chaos.vo.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-03-16 17:03
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListVo {
    @NonNull
    private Integer pageNum;

    @NonNull
    private Integer pageSize;

    private Boolean type;

    private String userName;
}
