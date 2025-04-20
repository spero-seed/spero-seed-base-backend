package com.speroseed.web.handler;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.speroseed.http.HttpCachingRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description 请求拦截器，记录请求日志
 * @author zfq
 * @date 2025/4/15 13:19
 */
@Slf4j
public class RequestLoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (log.isInfoEnabled()) {
            String method = request.getMethod();
            String uri = request.getRequestURI();
            Enumeration<String> headerNames = request.getHeaderNames();

            String params = getRequestParameters(request);
            log.info("[请求] 请求方式: {}, 请求路径: {}, 请求参数: {}", method, uri, params);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 记录响应信息
        if (log.isInfoEnabled()) {
            int status = response.getStatus();
            String responseBody = getResponseBody(response);
            log.info("[响应] 状态码: {}, 响应体: {}", status, responseBody);
        }
    }

    private String getRequestHeaders(HttpServletRequest request) {
        StringBuilder headerBuilder = new StringBuilder();

        // 获取所有请求头名称的枚举
        Enumeration<String> headerNames = request.getHeaderNames();
        headerBuilder.append("{\n");
        // 遍历并打印每个请求头
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            headerBuilder.append(StrUtil.format("{}:{}\n; ", headerName, headerValue));
        }
        headerBuilder.append("}");
        return headerBuilder.length() == 2 ? "无" : headerBuilder.toString();
    }

    private String getRequestParameters(HttpServletRequest request) {
        StringBuilder paramsBuilder = new StringBuilder();

        // 处理查询参数和表单参数
        Map<String, String[]> paramMap = request.getParameterMap();
        if (!paramMap.isEmpty()) {
            String queryParams = paramMap.entrySet().stream()
                    .map(entry -> entry.getKey() + "=" + String.join(",", entry.getValue()))
                    .collect(Collectors.joining("&"));
            paramsBuilder.append(queryParams);
        }

        // 处理请求体（如JSON）
        // 记录JSON等请求体内容，需通过过滤器缓存请求
        if (request instanceof HttpCachingRequestWrapper) {
            HttpCachingRequestWrapper wrappedRequest = (HttpCachingRequestWrapper) request;
            ServletInputStream wrappedRequestInputStream = null;
            try {
                wrappedRequestInputStream = wrappedRequest.getInputStream();
            } catch (IOException e) {
                throw new RuntimeException("获取请求体数据流失败", e);
            }
            String body = IoUtil.read(wrappedRequestInputStream, StandardCharsets.UTF_8);
            if (StrUtil.isNotEmpty(body)) {
                if (paramsBuilder.length() > 0) paramsBuilder.append("; ");
                paramsBuilder.append("Body: ").append(body);
            }
        }

        return paramsBuilder.length() == 0 ? "无" : paramsBuilder.toString();
    }

    private String getResponseBody(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper) {
            ContentCachingResponseWrapper wrappedResponse = (ContentCachingResponseWrapper) response;
            byte[] content = wrappedResponse.getContentAsByteArray();
            if (content.length > 0) {
                return new String(content, StandardCharsets.UTF_8);
            }
        }
        return "无";
    }
}
