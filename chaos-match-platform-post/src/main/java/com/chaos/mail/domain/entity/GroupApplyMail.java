package com.chaos.mail.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : wzq
 * @since : 2024-05-19 16:31
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupApplyMail implements IMail{
    //社群ID
    private Long groupId;
    //社群名称
    private String groupName;
    //入群人员
    private Long userId;
    //入群人员名称
    private String userName;
    //入群人员头像
    private String userAvatar;
}
