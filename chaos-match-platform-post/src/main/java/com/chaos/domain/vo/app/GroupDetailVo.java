package com.chaos.domain.vo.app;

import com.chaos.domain.bo.GroupOwnerBo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-05-09 23:23
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDetailVo {
    //主键ID@TableId
    private Long id;

    //社区名称
    private String name;
    //社群图标
    private String icon;
    //标签
    private String label;
    //介绍
    private String introduction;
    //群主
    private GroupOwnerBo owner;
}
