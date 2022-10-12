package com.coder.springmvc.servlet;

import annotation.Servlet;
import cn.coder.spring.bean.BeansException;
import cn.coder.spring.bean.factory.BeanFactory;
import cn.coder.spring.bean.factory.BeanFactoryAware;
import cn.coder.spring.bean.factory.ConfigurableListableBeanFactory;
import cn.coder.spring.context.support.ClassPathXmlApplicationContext;
import cn.coder.spring.stereotype.Component;
import com.coder.springmvc.context.WebApplicationContext;
import entity.HttpServletRequest;
import entity.HttpServletResponse;
import servlet.HttpServlet;

import java.io.IOException;

@Servlet("/")
public class DispatcherServlet extends HttpServlet {

    private WebApplicationContext webApplicationContext;

    @Override
    public void init() throws InstantiationException, IllegalAccessException {
        // 加载spring容器
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");

        // 加载初始化参数
//        String contextConfigLocation = this.getInitParameter("contextConfigLocation");
        String contextConfigLocation = "com.coder";

        // 加载容器,并读取配置文件
        webApplicationContext = new WebApplicationContext(contextConfigLocation, applicationContext);

        // 初始化容器
        webApplicationContext.refresh();

        // 加载对象
        webApplicationContext.excuteInstance();

        // 注入对象
        webApplicationContext.excuteAutoWried();

        // 请求映射
        webApplicationContext.initHandleMapping();

    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        webApplicationContext.excuteDispacth(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
        doGet(request, response);
    }

}
