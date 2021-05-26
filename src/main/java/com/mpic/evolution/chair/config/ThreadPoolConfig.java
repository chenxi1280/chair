package com.mpic.evolution.chair.config;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * 类名称： ThreadPoolConfig
 *
 * @author: admin
 * @date 2021/5/25 15:39
 * 类描述：
 */
@Configuration
public class ThreadPoolConfig {
    @Bean
    public ExecutorService getThreadPool(){
        int size =5;
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNamePrefix("thread-call-copy-video-url-runner-%d").build();

        return new ThreadPoolExecutor(size,size,0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(),namedThreadFactory);
    }
}