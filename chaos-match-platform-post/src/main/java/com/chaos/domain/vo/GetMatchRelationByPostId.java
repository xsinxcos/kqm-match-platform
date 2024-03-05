package com.chaos.domain.vo;

import com.chaos.feign.bo.AuthUserBo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-03-05 21:00
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetMatchRelationByPostId {
    List<MatchedUserVo> matchedUsers;
}
