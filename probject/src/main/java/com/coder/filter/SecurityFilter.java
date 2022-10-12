package com.coder.filter;

import annotation.Filter;
import cn.coder.spring.stereotype.Component;
import com.coder.RiemannRedisClient;
import com.coder.RiemannRedisClientTest;
import entity.HttpServletRequest;
import entity.HttpServletResponse;
import filterChain.FilterChain;
import servlet.HttpFilter;

import java.io.IOException;

@Filter("/")
public class SecurityFilter extends HttpFilter {

    @Override
    public boolean doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException {
        System.out.println("------ 安全认证过滤器 ------");

        // 放行
        if (request.getRequestURI().contains("register")) return true;

        // 拦截
        if (!request.getRequestURI().contains("login")) {
            String token = request.getHeader("token");
            System.out.println("token: " + token);
            if (token == null) return false;

            RiemannRedisClient client = new RiemannRedisClient();
            String token1 = client.get("token");

            System.out.println("redis-token: " + token1);
            return token.equals(token1);
        }
        return true;
    }

}
