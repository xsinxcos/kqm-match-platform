package com.chaos.constants;

/**
 * @description: 消息状态
 * @author: xsinxcos
 * @create: 2024-01-24 04:08
 **/
public class MessageConstants {
    public static class MessageStatusConstants {
        public static final Integer MESSAGE_UNDELIVERED = 0;
        public static final Integer MESSAGE_DELIVERED = 1;
        public static final String MESSAGE_SEND_SUCCESS = "success";
        public static final Integer MESSAGE_TYPE_TEXT = 0;
    }

    public final static String OFFLINE_MESSAGE_REDIS_KEY = "offline:";

    //每个用户的离线消息数量
    public final static Integer USER_MAX_NUMBER_MESSAGE = 1000;
}
