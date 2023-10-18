package com.yupi.springbootinit.config;



import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;


@Configuration
@Slf4j
public class ThreadPoolExecutorConfig {


    @Bean
    public ThreadPoolExecutor threadPoolExecutor(){
        ThreadFactory threadFactory = new ThreadFactory() {
            private int cnt;
            @Override
            public Thread newThread(@NotNull Runnable r) {
                // r 放入线程
                Thread thread = new Thread(r);
                thread.setName("线程："+cnt);
                log.info("进程创建："+thread.getName());
                cnt++;
                return thread;
            }
        };

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2,
                4,100, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10),
                threadFactory
                );
        return threadPoolExecutor;
    }
}
