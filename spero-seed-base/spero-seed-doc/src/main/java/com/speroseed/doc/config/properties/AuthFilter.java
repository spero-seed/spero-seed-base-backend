package com.speroseed.doc.config.properties;

import lombok.Data;
import org.springdoc.core.Constants;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 * @author zfq
 */
@Data
@ConfigurationProperties(prefix = "springdoc.auth")
public class AuthFilter {

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
