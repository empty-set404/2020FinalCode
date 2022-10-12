package com.coder.springmvc.context;

import cn.coder.spring.bean.BeansException;
import cn.coder.spring.bean.factory.BeanFactory;
import cn.coder.spring.bean.factory.BeanFactoryAware;
import cn.coder.spring.bean.factory.BeanNameAware;
import cn.coder.spring.bean.factory.ConfigurableListableBeanFactory;
import cn.coder.spring.bean.factory.annotation.Autowired;
import cn.coder.spring.bean.factory.config.BeanDefinition;
import cn.coder.spring.bean.factory.support.DefaultListableBeanFactory;
import cn.coder.spring.context.support.ClassPathXmlApplicationContext;
import cn.coder.spring.stereotype.Component;
import com.alibaba.fastjson.JSON;
import com.coder.entity.User;
import com.coder.mapper.UserMapper;
import com.coder.mybatis.annotation.Mapper;
import com.coder.mybatis.session.SqlSession;
import com.coder.mybatis.session.SqlSessionFactory;
import com.coder.mybatis.session.SqlSessionFactoryBuilder;
import com.coder.service.Impl.UserServiceImpl;
import com.coder.springmvc.handler.Handle;
import com.coder.springmvc.annotation.*;
import com.coder.springmvc.exception.ContextException;
import entity.HttpServletRequest;
import entity.HttpServletResponse;

import java.io.File;
import java.lang.reflect.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebApplicationContext {

    private String contextConfigLocation;

    private List<String> classNameList = new ArrayList<>();

    private Map<String, Object> container = new ConcurrentHashMap<>();

    private List<Handle> handles = new ArrayList<>();

    private ClassPathXmlApplicationContext applicationContext;

    public WebApplicationContext() {
    }

    public WebApplicationContext(String contextConfigLocation, ClassPathXmlApplicationContext applicationContext) {
        this.contextConfigLocation = contextConfigLocation;
        this.applicationContext = applicationContext;
    }

    /**
     * spring 感知对象 BeanFactoryAware 获取到 beanFactory
     * @param
     * @throws BeansException
     */
    public void refresh() {
//        String basePack = XmlParse.getBasePack(contextConfigLocation);
        String basePack = "com.coder.controller,com.coder.mapper";
        String[] split = basePack.split(",");
        for (String path : split) {
            excuteScanPackage(path);
        }
    }

    // 扫描文件
    public void excuteScanPackage(String pack) {
        URL resource = this.getClass().getClassLoader().getResource(pack.replaceAll("\\.", "/"));
        if (resource != null) {
            String path = resource.getFile();
            File file = new File(path);
            for (File listFile : file.listFiles()) {
                if (listFile.isDirectory()) {
                    // 递归调用
                    excuteScanPackage(pack + "." + listFile.getName());
                }else {
                    String className = pack + "." + listFile.getName().replaceAll(".class", "");
                    this.classNameList.add(className);
                }
            }
        }
    }

    // 实例化对象
    public void excuteInstance() {
        try {
            if (this.classNameList.size() == 0) {
                throw new ContextException("not bean init");
            }
            for (String className : this.classNameList) {
                Class<?> clazz = Class.forName(className);
                // 判断注解类型
                if (clazz.isAnnotationPresent(Controller.class)) {
                    // 首字母转小写
                    String beanName = clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1);
                    container.put(beanName, clazz.newInstance());
                }else if(clazz.isAnnotationPresent(Service.class)) {
                    // 获取注解
                    Service annotation = clazz.getAnnotation(Service.class);
                    if("".equals(annotation.value())) {
                        Class<?>[] interfaces = clazz.getInterfaces();
                        for (Class<?> anInterface : interfaces) {
                            String beanName = anInterface.getSimpleName().substring(0 ,1).toLowerCase() + anInterface.getSimpleName().substring(1);
                            container.put(beanName, clazz.newInstance());
                        }
                    }else {
                        String beanName = clazz.getSimpleName().substring(0 ,1).toLowerCase() + clazz.getSimpleName().substring(1);
                        container.put(beanName, clazz.newInstance());
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 对象注入
    public void excuteAutoWried() {
        try {
            for (Map.Entry<String, Object> entry : container.entrySet()) {
                Object bean = entry.getValue();
                // 获取所有属性
                Field[] declaredFields = bean.getClass().getDeclaredFields();
                for (Field declaredField : declaredFields) {
                    // 判断是否有autowried注解
                    if (declaredField.isAnnotationPresent(Autowired.class)) {
                        // 获取注解
                        Class<?> type = declaredField.getType();
                        String beanName = type.getSimpleName().substring(0 ,1).toLowerCase() + type.getSimpleName().substring(1);

                        // 注入对象
                        declaredField.setAccessible(true);
                        declaredField.set(bean, applicationContext.getBean(beanName));
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 请求映射
    public void initHandleMapping() {
        if (container.isEmpty()) {
            throw new ContextException("ioc is not bean");
        }
        for (Object value : container.values()) {
            // 判断是否有controller注解
            Class<?> clazz = value.getClass();
            if (clazz.isAnnotationPresent(Controller.class)) {
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    // 判斷是否有requestMapping注解
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        // 獲取注解
                        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                        String url = annotation.value();

                        // 加入到handlers
                        Handle handle = new Handle(url, value, method);
                        handles.add(handle);
                    }
                }
            }
        }
    }

    // 请求转发
    public void excuteDispacth(HttpServletRequest request, HttpServletResponse response) {
        try {
            Handle handler = getHandler(request);
            if (handler == null) {
                response.getOutputStream().write("<h1>404 not found<h1>".getBytes());
                return;
            }
            // 獲取參數列表
            Method method = handler.getMethod();
            Class<?>[] parameterTypes = method.getParameterTypes();
            Object[] params = new Object[parameterTypes.length];

            // 内置对象注入
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> parameterType = parameterTypes[i];
                if (HttpServletRequest.class.equals(parameterType)) {
                    params[i] = request;
                }
                if (HttpServletResponse.class.equals(parameterType)) {
                    params[i] = response;
                }
            }

            Map<String, List<Object>> parameterMap = request.getParams();
            for (Map.Entry<String, List<Object>> entry : parameterMap.entrySet()) {
                String paramName = entry.getKey();
                List<Object> value = entry.getValue();

                // 注入参数
                int index = hasRequestParm(method, paramName);
                if (index != -1) {
                    params[index] = value.get(0);
                }else {
                    List<String> paramterList = getParamterNames(method);
                    for (int i = 0; i < paramterList.size(); i++) {
                        if (paramName.equals(paramterList.get(i))) {
                            params[i] = value.get(0);
                            break;
                        }
                    }
                }
            }

            // 調用方法
            response.setHeader("Content-Type", "application/json;charset=utf-8");
            Object invoke = method.invoke(handler.getController(), params);

            // 处理返回值，统一json返回
            String json = JSON.toJSONString(invoke);
            response.getOutputStream().write(json.getBytes());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 根据请求获取对应的handler
    public Handle getHandler(HttpServletRequest request) {
        for (Handle handle : handles) {
            // 匹配 /user/query
            String requestURI = request.getRequestURI();
            if (handle.getUrl().equals(requestURI)) {
                return handle;
            }
            // 匹配 user/query
            if (requestURI.equals("/" + handle.getUrl())) {
                return handle;
            }
        }
        return null;
    }

    // 判断方法上的参数
    public int hasRequestParm(Method method, String name) {
        // 判断是否有requestMapping注解
        if (method.isAnnotationPresent(RequestMapping.class)) {
            Parameter[] parameters = method.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                Parameter param = parameters[i];
                // 判断requestParam注解
                if (param.isAnnotationPresent(RequestParam.class)) {
                    RequestParam annotation = param.getAnnotation(RequestParam.class);
                    String value = annotation.value();
                    if (name.equals(value)) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    public List<String> getParamterNames(Method method) {
        List<String> list = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for (Parameter parameter : parameters) {
            list.add(parameter.getName());
        }
        return list;
    }


}
