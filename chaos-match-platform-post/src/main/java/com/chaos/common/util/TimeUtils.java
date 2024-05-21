package com.chaos.common.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * 时间工具类
 *
 * @author : wzq
 * @since : 2024-05-15 23:33
 **/
public class TimeUtils {
    public static boolean isSameDay(long timestamp1, long timestamp2) {
        // 将时间戳转换为 Instant 对象
        Instant instant1 = Instant.ofEpochMilli(timestamp1);
        Instant instant2 = Instant.ofEpochMilli(timestamp2);

        // 将 Instant 对象转换为 LocalDate 对象，忽略时间
        LocalDate date1 = instant1.atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate date2 = instant2.atZone(ZoneId.systemDefault()).toLocalDate();

        // 比较两个 LocalDate 对象是否相等
        return date1.equals(date2);
    }
}
