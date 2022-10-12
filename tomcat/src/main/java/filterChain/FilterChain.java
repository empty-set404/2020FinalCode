package filterChain;

import container.FilterContainer;
import entity.HttpServletRequest;
import entity.HttpServletResponse;
import servlet.HttpFilter;
import servlet.HttpServlet;
import servlet.NoFoundServlet;
import servlet.PageServlet;
import utils.StringUtil;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class FilterChain {

    private static Logger logger = Logger.getLogger(FilterChain.class.getSimpleName());

    private HttpServlet servlet;

    public FilterChain(HttpServlet servlet) {
        this.servlet = servlet;
    }

    /**
     * 当所有过滤器通过时，才执行servlet的代码
     * @param request
     * @param response
     * @throws IOException
     */
    public void doFilter(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info(new Date() + " 请求uri: "  + request.getRequestURI() + " 请求参数: " + request.getParams() + " " + Thread.currentThread().getName());

        // 执行过滤器
        List<HttpFilter> container = FilterContainer.FilterContainer;
        for (int i = 0; i < container.size(); i++) {
            HttpFilter filter = container.get(i);
            if (StringUtil.isAntMatch(request, filter)) {
                if (!filter.doFilter(request, response, this)) {
                    new NoFoundServlet().doService(request, response);
                    return;
                }
            }
        }

        // 处理静态资源
        if (servlet == null && (request.getRequestURI().contains(".html"))) {
            new PageServlet().doService(request, response);
            return;
        }

        // 路径不存在
        if (servlet == null || "/favicon.ico".equals(request.getRequestURI())) {
            new NoFoundServlet().doService(request, response);
            return;
        }

        servlet.doService(request, response);
    }

}
