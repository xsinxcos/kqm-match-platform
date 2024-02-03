package com.chaos.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: matchmentBo
 * @author: xsinxcos
 * @create: 2024-02-04 03:40
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchBo {
    private Long matchFrom;
    private Long matchTo;
    private Long matchPost;
}
