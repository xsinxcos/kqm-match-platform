package com.chaos.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @description: 异步线程池配置
 * @author: xsinxcos
 * @create: 2024-03-04 23:50
 **/
@Configuration
@EnableAsync
public class AsyncThreadPoolConfig {
    /**
     * CPU 核数
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    /**
     * IO 处理线程数
     */
    private static final int IO_MAX = Math.max(2, 2 * CPU_COUNT);
    /**
     * 空闲线程最大保活时限，单位为秒
     */
    private static final int KEEP_ALIVE_SECOND = 60;
    /**
     * 有界阻塞队列容量上限
     */
    private static final int QUEUE_SIZE = 10000;
    @Bean("asyncExecutor")
    public Executor asyncThreadPool(){
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(
                Math.max(1, IO_MAX / 5),
                IO_MAX,
                KEEP_ALIVE_SECOND,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(QUEUE_SIZE),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
       return poolExecutor;
    }
}
