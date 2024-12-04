package com.hlw.filter;

import com.alibaba.fastjson.JSONObject;
import com.hlw.pojo.Result;
import com.hlw.utils.JwtUtils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;

/**
 * 管理员过滤器，用于验证管理员的登录状态和身份
 */
@Slf4j
@WebFilter(urlPatterns = "/administrator")
public class Administratorfilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        // 将 ServletRequest 和 ServletResponse 转换为 HttpServletRequest 和 HttpServletResponse
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 获取请求的 URL
        String url = request.getRequestURL().toString();
        log.info("请求的url：{}", url);

        // 获取请求头中的 Token
        String jwt = request.getHeader("token");

        // 检查 Token 是否存在
        if (!StringUtils.hasLength(jwt)) {
            log.info("请求头中的 token 为空，返回未登录信息");
            Result error = Result.error("NOT_LOGIN");
            response.getWriter().write(JSONObject.toJSONString(error));
            return;
        }

        // 尝试解析 Token
        try {
            // 使用工具类解析 JWT，并获取解码后的信息
            Map<String, Object> decodedJwt = JwtUtils.parseJWT(jwt);
            if (decodedJwt != null) {
                log.info("JWT 解析成功，解码信息：{}", decodedJwt);
                // 将解码后的信息存入请求属性
                request.setAttribute("jobType", decodedJwt.get("jobType"));
                // 检查用户是否为后台管理员
                if(decodedJwt.get("jobType")==null||!"后台管理员".equals(decodedJwt.get("jobType"))) {
                    return ;
                }
            }
        } catch (Exception e) {
            log.info("解析令牌失败，返回未登录错误信息");
            Result error = Result.error("NOT_LOGIN");
            response.getWriter().write(JSONObject.toJSONString(error));
            return;
        }

        // Token 合法，放行请求
        log.info("令牌合法，放行");
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
