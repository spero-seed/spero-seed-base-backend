package com.speroseed.doc.config.properties;

import com.github.xiaoymin.knife4j.core.conf.GlobalConstants;
import com.github.xiaoymin.knife4j.extend.filter.basic.ServletSecurityBasicAuthFilter;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.StringUtils;

/**
 *
 * @author zfq
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({DocProperties.class, AuthFilter.class})
public class DocAutoConfiguration {

    @Autowired
    private DocProperties docProperties;

    @Bean
    public OpenAPI openAPI() {

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
    public ServletSecurityBasicAuthFilter securityBasicAuthFilter(AuthFilter authFilter) {

        ServletSecurityBasicAuthFilter servletSecurityBasicAuthFilter = new ServletSecurityBasicAuthFilter();
        servletSecurityBasicAuthFilter.setEnableBasicAuth(true);
        servletSecurityBasicAuthFilter.setUserName(StringUtils.hasText(authFilter.getUsername()) ?
                authFilter.getUsername() : GlobalConstants.BASIC_DEFAULT_USERNAME);
        servletSecurityBasicAuthFilter.setPassword(StringUtils.hasText(authFilter.getPassword()) ?
                authFilter.getPassword() : GlobalConstants.BASIC_DEFAULT_PASSWORD);
        return servletSecurityBasicAuthFilter;
    }
}

