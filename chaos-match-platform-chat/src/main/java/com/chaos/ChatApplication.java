package com.chaos;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableFeignClients
@EnableAsync
@MapperScan("com.chaos.mapper")
public class ChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class ,args);
    }
}