package com.speroseed.core.config;

import com.speroseed.core.config.properties.CoreProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
@Slf4j
@EnableConfigurationProperties({CoreProperties.class})
public class ThreadPoolConfig {

    @Autowired
    private CoreProperties coreProperties;

    @Bean("speroseedThreadPoolTaskExecutor")
    public ThreadPoolTaskExecutor speroseedThreadPoolTaskExecutor() {

        Integer poolSize = coreProperties.getCommonThreadPoolSize();
        if (poolSize == null) {
            int cpuCores = Runtime.getRuntime().availableProcessors();
            log.info("当前操作系统拥有的cpu逻辑核心数 : {}", cpuCores);
            poolSize = cpuCores * 2;
        }
        log.info("spThreadPoolTaskExecutor任务线程池创建的线程数量：{}", poolSize);

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(poolSize);
        executor.setMaxPoolSize(poolSize);
        executor.setQueueCapacity(Integer.MAX_VALUE);
//        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("spThreadPoolTaskExecutor-");
        executor.initialize();
        return executor;
    }
}
