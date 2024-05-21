package com.chaos.post.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author : wzq
 * @since : 2024-05-18 20:57
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetLiveDetailVo {
    private Long id;

    private Long postedId;

    private String posterUsername;

    private String posterAvatar;

    private String title;

    private String content;

    private Date time;
}
