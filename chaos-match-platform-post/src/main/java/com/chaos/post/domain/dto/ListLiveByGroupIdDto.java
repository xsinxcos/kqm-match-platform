package com.chaos.post.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : wzq
 * @since : 2024-05-18 20:06
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListLiveByGroupIdDto {

    private Integer pageNum;

    private Integer pageSize;

    private Long groupId;
}
