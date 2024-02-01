package com.chaos.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * (Tag)表实体类
 *
 * @author chaos
 * @since 2024-02-01 08:24:25
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_tag")
public class Tag {
    @TableId
    private Long id;

    //标签名
    private String name;

    private Long createBy;

    private Date createTime;

    private Long updateBy;

    private Date updateTime;
    //删除标志（0为未删除，1为已删除）
    private Integer delFlag;


}

