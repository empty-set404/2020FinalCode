package cn.coder.spring.context.annotation;

import cn.coder.spring.bean.factory.config.BeanDefinition;
import cn.coder.spring.stereotype.Component;
import cn.hutool.core.util.ClassUtil;

import java.util.LinkedHashSet;
import java.util.Set;

public class ClassPathScanningCandidateComponentProvider {

    protected Set<BeanDefinition> findCandidateComponents(String basePackage) {
        Set<BeanDefinition> list = new LinkedHashSet<>();
        Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation(basePackage, Component.class);
        for (Class<?> clazz : classes) {
            BeanDefinition beanDefinition = new BeanDefinition(clazz);
            list.add(beanDefinition);
        }
        return list;
    }

}
