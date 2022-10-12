package com.coder.postProcessor;

import cn.coder.spring.bean.BeansException;
import cn.coder.spring.bean.factory.ConfigurableListableBeanFactory;
import cn.coder.spring.bean.factory.config.BeanFactoryPostProcessor;
import cn.coder.spring.stereotype.Component;
import com.coder.mybatis.annotation.Mapper;
import com.coder.mybatis.session.SqlSession;
import com.coder.mybatis.session.SqlSessionFactory;
import com.coder.mybatis.session.SqlSessionFactoryBuilder;
import com.coder.service.Impl.UserServiceImpl;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
public class MapperProxy implements BeanFactoryPostProcessor {

    private List<String> classNameList = new ArrayList<>();

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // 扫描mapper注解创建代理对象并注入容器
        excuteScanPackage("com.coder.mapper");
        try {
            for (int i = 0; i < this.classNameList.size(); i++) {
                String className = this.classNameList.get(i);
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(Mapper.class)) {
                    SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
                    SqlSessionFactory factory = builder.build(UserServiceImpl.class.getClassLoader().getResourceAsStream("mybatis-config.xml"));
                    SqlSession session = factory.openSession();
                    Object bean = session.getMapper(clazz);
                    beanFactory.registerSingleton(clazz.getSimpleName().substring(0,1).toLowerCase() + clazz.getSimpleName().substring(1), bean);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
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

}
