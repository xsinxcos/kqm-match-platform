package com.chaos.team.domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (TeamUser)表实体类
 *
 * @author chaos
 * @since 2024-05-14 23:38:01
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_team_user")
public class TeamUser {

    //队伍ID
    private Long teamId;
    //用户ID
    private Long userId;
    //类型
    private Integer type;


}

