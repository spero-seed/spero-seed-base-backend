package com.speroseed.doc.config.condition;


import cn.hutool.core.collection.CollectionUtil;
import com.speroseed.doc.config.properties.DocProperties;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.Constants;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description
 * @author zfq
 * @date 2025/4/23 9:16
 */
@Slf4j
public class DocEnableCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String configPath = Constants.SPRINGDOC_PREFIX;
        String excludeProfilesPath = configPath + ".exclude-profiles";
        // 1. 从 Environment 中获取激活的 Profile 列表
        String[] activeProfiles = context.getEnvironment().getActiveProfiles();
        List<String> activeProfileList = Arrays.stream(activeProfiles).map(String::trim).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(activeProfileList)) {
            log.info("未配置 spring.profiles.active ，无法执行 {} 配置，因此开启接口文档", excludeProfilesPath);
            return true;
        }

        // 从 Environment 手动绑定配置
        DocProperties config = null;
        try {
            config = Binder.get(context.getEnvironment())
                    .bind(configPath, DocProperties.class)
                    .orElseThrow(() -> new IllegalStateException(configPath+"配置绑定失败"));
        } catch (Exception e) {
            log.error("{}配置读取失败", configPath, e);
        }
        if (config == null) {
            log.info("未配置{}，启动接口文档", configPath);
            return true;
        }
        log.info("判断是否开启接口文档功能时，所读取到的配置信息：activeProfiles: {}，{}：{}", activeProfileList, excludeProfilesPath, config.getExcludeProfiles());

        List<String> excludeProfileList = config.getExcludeProfiles();
        if (CollectionUtil.isNotEmpty(excludeProfileList)) {
            for (String excludeProfile : excludeProfileList) {
                if (activeProfileList.contains(excludeProfile.trim())) {
                    log.info("当前生效的环境匹配上排除名单中的环境值，故不启动接口文档。生效的环境：{}，被排除的环境：{}", activeProfileList, excludeProfile);
                    return false;
                }
            }
            log.info("当前生效的环境未匹配上排除名单中的环境值，启动接口文档。生效的环境：{}，排除名单：{}", activeProfileList, excludeProfileList);
            return true;
        } else {
            log.info("未设置 {} 配置，直接启动接口文档", excludeProfilesPath);
            return true;
        }
    }
}
