package com.speroseed.doc.config.properties;

import lombok.Data;
import org.springdoc.core.Constants;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @description springdoc文档
 * @author zfq
 * @date 2025/4/23 11:02
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
    private AuthProperties auth;

    /**
     * 环境排除名单
     * 列表环境下，不生成接口文档
     */
    private List<String> excludeProfiles;
}
