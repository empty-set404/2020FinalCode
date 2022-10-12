package com.coder.filter;

import annotation.Filter;
import entity.HttpServletRequest;
import entity.HttpServletResponse;
import filterChain.FilterChain;
import servlet.HttpFilter;

import java.io.IOException;
import java.util.HashMap;

/**
 * 跨域过滤器
 */
@Filter("/")
public class CorsFilter extends HttpFilter {

    @Override
    public boolean doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException {
        System.out.println("------ 跨越过滤器 ------");
        response.setHeader("Access-Control-Allow-Origin", "*"); //解决跨域访问报错
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600"); //设置过期时间
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // 支持HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // 支持HTTP 1.0. response.setHeader("Expires", "0");
        return true;
    }
}
