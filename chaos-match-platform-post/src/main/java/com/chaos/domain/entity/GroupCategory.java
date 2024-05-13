package com.chaos.domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 社群与类别关系表(GroupCategory)表实体类
 *
 * @author chaos
 * @since 2024-05-13 20:52:29
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_group_category")
public class GroupCategory {
    //社群ID@TableId
    private Long groupId;
    //类别ID@TableId
    private Long categoryId;


}

