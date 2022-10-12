package web;

import annotation.Filter;
import annotation.Servlet;
import config.TomcatConfig;
import container.FilterContainer;
import container.ServletContainer;
import servlet.HttpFilter;
import servlet.HttpServlet;
import socket.NioService;
import socket.TomcatService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class CoreApp implements Runnable {

    private static Class<?> aClass;

    // 解析注解
    public static void init(Class<?> mainClazz) throws IllegalAccessException, InstantiationException, ClassNotFoundException, IOException {
        aClass = mainClazz;
        new Thread(new CoreApp()).start();
    }

    private static void scanPackage(Class<?> clazz, String scanPackage, List<String> list) {
        URL url = clazz.getClassLoader().getResource(scanPackage.replaceAll("\\.", "/"));
        if (url != null) {
            File file = new File(url.getFile());
            for (File file1 : file.listFiles()) {
                if (file1.isDirectory()) {
                    if ("".equals(scanPackage)) {
                        scanPackage(scanPackage + file1.getName(), list);
                    }else {
                        scanPackage(scanPackage + "." + file1.getName(), list);
                    }
                }else {
                    if (file1.getName().endsWith(".class")) {
                        if ("".equals(scanPackage)) {
                            list.add(scanPackage + file1.getName().replaceAll(".class", ""));
                        }else {
                            list.add(scanPackage + "." + file1.getName().replaceAll(".class", ""));
                        }
                    }
                }
            }
        }
    }

    // 扫描文件
    private static void scanPackage(String scanPackage, List<String> list) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(scanPackage.replaceAll("\\.", "/"));
        if (url != null) {
            File file = new File(url.getFile());
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File file1 : file.listFiles()) {
                    if (file1 != null && file1.isDirectory()) {
                        if ("".equals(scanPackage)) {
                            scanPackage(scanPackage + file1.getName(), list);
                        }else {
                            scanPackage(scanPackage + "." + file1.getName(), list);
                        }
                    }else {
                        if (file1.getName().endsWith(".class")) {
                            if ("".equals(scanPackage)) {
                                list.add(scanPackage + file1.getName().replaceAll(".class", ""));
                            }else {
                                list.add(scanPackage + "." + file1.getName().replaceAll(".class", ""));
                            }
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException {
        CoreApp.init(web.CoreApp.class);
    }

    @Override
    public void run() {
        try {
            List<String> list = new ArrayList<>();
            scanPackage(aClass, "com.coder", list);

            for (String className : list) {
                Class<?> clazz = Class.forName(className);

                // 判断filter注解
                if (clazz.isAnnotationPresent(Filter.class)) {
                    // 获取过滤路径
                    Filter filter = clazz.getAnnotation(Filter.class);
                    HttpFilter bean = (HttpFilter) clazz.newInstance();
                    bean.setMapping(filter.value());
                    FilterContainer.pushFilter(bean);
                }

                // 判断Servlet注解
                if (clazz.isAnnotationPresent(Servlet.class)) {
                    // 获取请求路径
                    Servlet servlet = clazz.getAnnotation(Servlet.class);
                    HttpServlet bean = (HttpServlet) clazz.newInstance();
                    ServletContainer.pushServlet(servlet.value(), bean);
                }

            }

            // 执行初始化方法init
            ServletContainer.ServletContainer.forEach(new BiConsumer<String, HttpServlet>() {
                @Override
                public void accept(String s, HttpServlet servlet) {
                    try {
                        servlet.init();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            });

            // 启动服务
            TomcatService service = new NioService();
            service.openPort(TomcatConfig.HTTP_PORT, TomcatConfig.HTTP_SO_TIMEOUT);
            service.doService();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
