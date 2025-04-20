package com.speroseed.core.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 *
 * @author zfq
 */
@Data
@ConfigurationProperties(prefix = "speroseed")
public class CoreProperties {

    /**
     * 任务线程池大小
     */
    private Integer commonThreadPoolSize;
}
