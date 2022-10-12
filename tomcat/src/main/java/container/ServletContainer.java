package container;

import servlet.HttpServlet;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ServletContainer {

    public static final Map<String, HttpServlet> ServletContainer = new LinkedHashMap<>();

    public static void pushServlet(String path, HttpServlet servlet) {
        ServletContainer.put(path, servlet);
    }

    public static HttpServlet getServlet(String path){
        HttpServlet servlet = ServletContainer.get(path);
        if (servlet != null) {
            return servlet;
        }
        return isAntMatch(path);
    }

    // 路径匹配算法
    private static HttpServlet isAntMatch(String path) {
        HttpServlet servlet = null;
        for (String servletPath : ServletContainer.keySet()) {
            if (path.startsWith(servletPath)) {
                servlet = ServletContainer.get(servletPath);
            }
        }
        return servlet;
    }

}
