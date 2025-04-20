package com.speroseed.doc.config.properties;

import lombok.Data;
import org.springdoc.core.Constants;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 * @author zfq
 */
@Data
@ConfigurationProperties(prefix = Constants.SPRINGDOC_PREFIX)
public class DocProperties {

    /**
     * 标题
     */
    private String title = "";

    /**
     * 简介
     */
    private String description = "";

    /**
     * 作者
     */
    private String author;

    /**
     * 版本
     */
    private String version = "";

    /**
     * 权限验证
     */
    private AuthFilter auth;
}
