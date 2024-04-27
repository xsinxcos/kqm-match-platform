package com.chaos.spark.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-04-27 23:47
 **/
@Data
@AllArgsConstructor
public class SparkHistoryMessage {
    private List<SparkText> messages;

    private Long lastTime;

    public SparkHistoryMessage() {
        this.messages = new ArrayList<>();
        this.lastTime = -1L;
    }
}
