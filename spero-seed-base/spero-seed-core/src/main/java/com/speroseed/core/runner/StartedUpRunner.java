package com.speroseed.core.runner;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @description 系统启动完毕后的相关操作
 * @author zfq
 * @date 2025/4/2 12:39
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StartedUpRunner implements ApplicationRunner {

    private final ConfigurableApplicationContext context;

    @Value("${server.port:}")
    private String port;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (context.isActive()) {
            log.info("==> 系统启动完毕");
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                if (networkInterface.isLoopback() || networkInterface.isVirtual()) {
                    continue;
                }
                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                    InetAddress inetAddress = interfaceAddress.getAddress();
                    if (inetAddress instanceof Inet4Address) {
                        String url = String.format("http://%s:%s", inetAddress.getHostAddress(), StrUtil.isNotEmpty(port) ? port : "8080");
                        if (StrUtil.isNotBlank(contextPath)) {
                            url += contextPath;
                        }
                        log.info("==> 访问路径: {}", url);
                        log.info("==> 文档路径: {}/doc.html", url);
                    }
                }
            }
        }
    }
}
