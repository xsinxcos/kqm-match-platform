package com.chaos.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

//@Component
public class RabbitMQListener {
    @RabbitListener(queues = "simple.queue")
    public void ListenSimpleQueue(String msg){
        System.out.println("接收到的参数为" + "【" + msg + "】");
    }
}
