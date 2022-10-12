package com.coder.bean;

import cn.coder.spring.bean.BeansException;
import cn.coder.spring.bean.factory.ConfigurableListableBeanFactory;
import cn.coder.spring.bean.factory.config.BeanFactoryPostProcessor;
import cn.coder.spring.bean.factory.config.BeanPostProcessor;
import cn.coder.spring.stereotype.Component;

@Component
public class UserTest4 implements BeanPostProcessor {

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("...........");
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("--------------");
        return bean;
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("-------------------------------------");
    }
}
