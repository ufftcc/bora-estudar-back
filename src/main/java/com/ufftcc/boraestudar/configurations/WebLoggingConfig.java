package com.ufftcc.boraestudar.configurations;

import com.ufftcc.boraestudar.services.DiscordBotService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebLoggingConfig implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(DiscordBotService.class);

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
                log.info("Request: {} {}", request.getMethod(), request.getRequestURI());
                return true;
            }
        });
    }
}