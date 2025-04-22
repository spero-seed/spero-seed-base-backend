package com.speroseed.web.filter;

import com.speroseed.http.HttpCachingRequestWrapper;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.util.ContentCachingResponseWrapper;

public class CachingRequestFilter extends OncePerRequestFilter {

    /**
     * 缓存请求
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 包装请求和响应以缓存内容
        HttpCachingRequestWrapper wrappedRequest = new HttpCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
        filterChain.doFilter(wrappedRequest, wrappedResponse);
        // 必须将响应内容写回原始响应流
        wrappedResponse.copyBodyToResponse();
    }
}
