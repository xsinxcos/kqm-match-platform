package com.chaos.mail.domain.vo;

import com.chaos.mail.domain.entity.IMail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : wzq
 * @since : 2024-05-19 20:04
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListMailVo {
    private Long id;
    private Integer isRead;
    private Integer type;
    private String message;
}
