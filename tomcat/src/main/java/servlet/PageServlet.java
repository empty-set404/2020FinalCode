package servlet;

import entity.HttpServletRequest;
import entity.HttpServletResponse;

import java.io.InputStream;

public class PageServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String requestURI = request.getRequestURI();
        InputStream resourceAsStream = PageServlet.class.getClassLoader().getResourceAsStream(requestURI.substring(1));
        byte[] bytes = new byte[1024];
        int len = resourceAsStream.read(bytes);
        while((len != -1)) {
            response.getOutputStream().write(bytes, 0, len);
            len = resourceAsStream.read(bytes);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
        doGet(request, response);
    }
}
