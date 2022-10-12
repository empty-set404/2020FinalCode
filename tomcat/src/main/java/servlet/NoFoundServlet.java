package servlet;

import entity.HttpServletRequest;
import entity.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class NoFoundServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ByteArrayOutputStream outputStream = response.getOutputStream();
        outputStream.write(("<h1>页面不存在: " + request.getRequestURI() + "</h1>").getBytes());
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }

}
