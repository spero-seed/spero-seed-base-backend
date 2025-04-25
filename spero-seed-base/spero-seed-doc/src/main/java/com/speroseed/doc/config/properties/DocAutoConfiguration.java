package com.speroseed.doc.config.properties;

import com.github.xiaoymin.knife4j.core.conf.GlobalConstants;
import com.github.xiaoymin.knife4j.extend.filter.basic.ServletSecurityBasicAuthFilter;
import com.speroseed.doc.config.condition.DocEnableCondition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.StringUtils;

/**
 * @description 接口文档配置类
 * @author zfq
 * @date 2025/4/23 11:01
 */
@Slf4j
@Configuration
@Conditional({DocEnableCondition.class})
@EnableConfigurationProperties({DocProperties.class, AuthProperties.class})
public class DocAutoConfiguration {

    @Autowired
    private DocProperties docProperties;

    @Bean
    public OpenAPI openAPI() {
        log.info("=====》启动接口文档");
        Info info = new Info().title(docProperties.getTitle()) // 标题
                .description(StringUtils.hasText(docProperties.getDescription()) ?
                        docProperties.getDescription() : docProperties.getTitle()) // 简介
                .contact(new Contact().name(docProperties.getAuthor())) // 作者
                .version(docProperties.getVersion()); // 版本
        return new OpenAPI().info(info);
    }

    @Bean
    @Primary
    @ConditionalOnProperty(name = "springdoc.auth.enable", havingValue = "true")
    public ServletSecurityBasicAuthFilter securityBasicAuthFilter(AuthProperties authProperties) {

        ServletSecurityBasicAuthFilter servletSecurityBasicAuthFilter = new ServletSecurityBasicAuthFilter();
        servletSecurityBasicAuthFilter.setEnableBasicAuth(true);
        servletSecurityBasicAuthFilter.setUserName(StringUtils.hasText(authProperties.getUsername()) ?
                authProperties.getUsername() : GlobalConstants.BASIC_DEFAULT_USERNAME);
        servletSecurityBasicAuthFilter.setPassword(StringUtils.hasText(authProperties.getPassword()) ?
                authProperties.getPassword() : GlobalConstants.BASIC_DEFAULT_PASSWORD);
        return servletSecurityBasicAuthFilter;
    }
}

