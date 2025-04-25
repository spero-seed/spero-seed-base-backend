package com.speroseed.doc.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @description 权限认证
 * @author zfq
 * @date 2025/4/23 11:01
 */
@Data
@ConfigurationProperties(prefix = "springdoc.auth")
public class AuthProperties {

    /**
     * 是否开启
     */
    private boolean enable = true;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}
