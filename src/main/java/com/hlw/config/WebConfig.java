package com.hlw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类，用于全局的Web配置
 */
@Configuration
public class WebConfig {

    /**
     * 配置跨域请求处理
     *
     * @return WebMvcConfigurer 实例，用于配置Spring MVC的跨域设置
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            /**
             * 添加跨域映射规则
             *
             * @param registry Cors注册表，用于设置跨域策略
             */
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
