package com.chaos.spark.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description: 星火模型响应
 * @author: xsinxcos
 * @create: 2024-01-31 04:22
 **/
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SparkChatResponse {
    Header header;
    Payload payload;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Header {
        int code;
        int status;
        String sid;
        String message;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Payload {
        Choices choices;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Choices {
        int status;
        int seq;
        List<Text> text;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Text {
        String role;
        String content;
        int index;
    }
}
