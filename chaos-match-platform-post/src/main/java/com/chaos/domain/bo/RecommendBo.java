package com.chaos.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 推荐训练单元
 * @author: xsinxcos
 * @create: 2024-03-22 18:57
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendBo {
    //第一个元素
    private Long firstElement;
    //第二个元素
    private Long secondElement;
    //两者关联程度
    private Float value;
}
