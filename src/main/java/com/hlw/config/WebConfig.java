package com.hlw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 允许所有路径跨域
                        .allowedOrigins("http://localhost:5173") // 允许的前端来源
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的 HTTP 方法，包括预检 OPTIONS
                        .allowedHeaders("*") // 允许所有请求头
                        .allowCredentials(true); // 允许带有凭证（如 Cookie）
            }
        };
    }
}