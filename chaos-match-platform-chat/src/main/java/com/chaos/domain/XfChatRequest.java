package com.chaos.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description: 星火模型请求参数
 * @author: xsinxcos
 * @create: 2024-01-31 02:27
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class XfChatRequest {
    private Header header;

    private Parameter parameter;

    private Payload payload;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Header {
        private String app_id;
        private String uid;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Parameter {
        private Chat chat;
    }

    @Data
    public static class Chat {
        private final String domain = "generalv3.5";
        private final float temperature = 0.5F;
        private final Integer max_tokens = 1024;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Payload {
        private Message message;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Message {
        private List<Text> text;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Text {
        private String role;
        private String content;
    }
}
