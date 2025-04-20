package com.speroseed.web.filter;

import org.slf4j.MDC;
import javax.servlet.*;
import java.io.IOException;
import java.util.UUID;

public class RequestTrackFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        try {
            // 生成请求 ID
            String requestId = UUID.randomUUID().toString();

            // 将 trackId 存入 MDC
            MDC.put("trackId", requestId);

            // 继续处理请求
            chain.doFilter(request, response);

        } finally {
            // 请求结束后清理 MDC，防止内存泄漏
            MDC.clear();
        }
    }
}
