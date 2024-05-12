package com.chaos.template;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-05-12 20:15
 **/
public class RedisKeyTemplate {

    public static String matchKey(long from, long to, long postId) {
        StringBuilder sb = new StringBuilder();
        StringBuilder key = sb.append("USER:")
                .append(from)
                .append(" invite USER:")
                .append(to)
                .append("with ")
                .append("POST:")
                .append(postId);
        return key.toString();
    }

    public static String GroupJoinKey(long from, long to, long postId, long groupId) {
        StringBuilder sb = new StringBuilder();
        StringBuilder key = sb.append("USER:")
                .append(from)
                .append(" applies to")
                .append(" USER:")
                .append(to)
                .append(" add")
                .append(" POST:")
                .append(postId)
                .append(" to")
                .append(" GROUP:")
                .append(groupId);
        return key.toString();
    }
}
