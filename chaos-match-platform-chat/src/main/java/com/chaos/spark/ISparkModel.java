package com.chaos.spark;

/**
 * @description:
 * @author: xsinxcos
 * @create: 2024-04-27 23:34
 **/
public interface ISparkModel {
    void sendMessage(Long id,String text);

    String getAnswer(Long id);

}
