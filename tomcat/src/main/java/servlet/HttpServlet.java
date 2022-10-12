package servlet;

import constant.HttpMethod;
import entity.HttpServletRequest;
import entity.HttpServletResponse;

public abstract class HttpServlet {

    public void init() throws InstantiationException, IllegalAccessException {};

    public abstract void doGet(HttpServletRequest request, HttpServletResponse response) throws Exception;

    public abstract void doPost(HttpServletRequest request, HttpServletResponse response) throws Exception;

    public void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (HttpMethod.GET.equals(request.getMethod())) {
            doGet(request, response);
        }
        if (HttpMethod.POST.equals(request.getMethod())) {
            doPost(request, response);
        }
    }
}

