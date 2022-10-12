package proccess;

import builder.HttpBuilder;
import container.ServletContainer;
import filterChain.FilterChain;
import servlet.HttpServlet;

public class TomcatProccess {

    public static void doService(HttpBuilder builder) throws Exception {
        HttpServlet servlet = ServletContainer.getServlet(builder.getRequest().getRequestURI());
        FilterChain filterChain = new FilterChain(servlet);
        filterChain.doFilter(builder.getRequest(), builder.getResponse());
    }

}
