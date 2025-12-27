package com.readingweb.config;

import com.readingweb.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.List;

/**
 * JWT认证拦截器
 * 
 * @author ReadingWeb Team
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationInterceptor implements HandlerInterceptor {
    
    private final JwtUtil jwtUtil;
    
    /**
     * 不需要认证的路径
     */
    private static final List<String> EXCLUDE_PATHS = Arrays.asList(
        "/auth/login",
        "/auth/register",
        "/test/hello"
    );
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        
        // 排除不需要认证的路径
        if (EXCLUDE_PATHS.stream().anyMatch(path::contains)) {
            return true;
        }
        
        // 获取token
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"未授权，请先登录\"}");
            return false;
        }
        
        // 移除Bearer前缀
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        // 验证token
        if (!jwtUtil.validateToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"Token无效或已过期\"}");
            return false;
        }
        
        // 将用户信息放入请求属性中
        try {
            String userId = jwtUtil.getClaimsFromToken(token).get("userId").toString();
            request.setAttribute("userId", Long.parseLong(userId));
            String username = jwtUtil.getClaimsFromToken(token).get("username").toString();
            request.setAttribute("username", username);
        } catch (Exception e) {
            log.error("解析token失败", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"Token解析失败\"}");
            return false;
        }
        
        return true;
    }
}

