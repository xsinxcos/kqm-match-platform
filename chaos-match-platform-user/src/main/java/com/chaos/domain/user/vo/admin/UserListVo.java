package com.chaos.domain.user.vo.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-03-16 17:40
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserListVo {
    private Long uid;

    //用户名
    private String userName;

    //手机号码
    private String phoneNumber;
    //用户头像
    private String avatar;
    //0为普通用户 1为管理员
    private Integer type;
    //性别 0为未知 1为男 2为女
    private Integer sex;
    //状态 0:启用 1:禁用
    private Integer status;
}
