package com.speroseed.web.config;

import com.speroseed.web.filter.CachingRequestFilter;
import com.speroseed.web.filter.RequestTrackFilter;
import com.speroseed.web.handler.RequestLoggingInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 为spring mvc注册拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 打印请求日志
        registry.addInterceptor(new RequestLoggingInterceptor()).addPathPatterns("/**");
    }

    /**
     * 缓存请求
     * @return
     */
    @Bean
    public FilterRegistrationBean<CachingRequestFilter> loggingFilter() {
        FilterRegistrationBean<CachingRequestFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CachingRequestFilter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

    /**
     * 通过requestId,跟踪请求
     * @return
     */
    @Bean
    public FilterRegistrationBean<RequestTrackFilter> requestTrackFilter() {
        FilterRegistrationBean<RequestTrackFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestTrackFilter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}