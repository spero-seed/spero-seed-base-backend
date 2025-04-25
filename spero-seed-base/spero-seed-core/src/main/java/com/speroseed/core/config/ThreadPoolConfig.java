package com.speroseed.core.config;

import com.speroseed.core.config.properties.SperoseedCoreProperties;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;

/**
 * @description 配置任务线程池
 * @author zfq
 * @date 2025/4/20 13:27
 */
@Configuration
@EnableAsync
@Slf4j
@EnableConfigurationProperties({SperoseedCoreProperties.class})
public class ThreadPoolConfig {

    @Autowired
    private SperoseedCoreProperties coreProperties;

    @Bean
    public TaskDecorator speroseedTaskDecorator() {
        return new TaskDecorator() {
            @Override
            public Runnable decorate(Runnable runnable) {
                // 获取主线程的 MDC 内容
                Map<String, String> contextMap = MDC.getCopyOfContextMap();
                return () -> {
                    try {
                        // 将 MDC 内容设置到子线程
                        MDC.setContextMap(contextMap);
                        runnable.run();
                    } finally {
                        // 任务结束后清理 MDC
                        MDC.clear();
                    }
                };
            }
        };
    }

    @Bean("speroseedThreadPoolTaskExecutor")
    public ThreadPoolTaskExecutor speroseedThreadPoolTaskExecutor() {

        Integer poolSize = coreProperties.getThreadPoolSize();
        if (poolSize == null) {
            int cpuCores = Runtime.getRuntime().availableProcessors();
            log.info("当前操作系统拥有的cpu逻辑核心数 : {}", cpuCores);
            poolSize = cpuCores * 2;
        }
        log.info("spThreadPoolTaskExecutor任务线程池创建的线程数量：{}", poolSize);

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setTaskDecorator(speroseedTaskDecorator());
        executor.setCorePoolSize(poolSize);
        executor.setMaxPoolSize(poolSize);
        executor.setQueueCapacity(Integer.MAX_VALUE);
        executor.setThreadNamePrefix("spThreadPoolTaskExecutor-");
        executor.initialize();
        return executor;
    }
}
