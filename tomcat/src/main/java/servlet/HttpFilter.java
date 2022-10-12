package servlet;

import entity.HttpServletRequest;
import entity.HttpServletResponse;
import filterChain.FilterChain;

import java.io.IOException;

public abstract class HttpFilter {

    private String mapping;

    public String getMapping() {
        return mapping;
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

    public abstract boolean doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException;

}
