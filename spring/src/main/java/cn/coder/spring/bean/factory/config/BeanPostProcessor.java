package cn.coder.spring.bean.factory.config;

import cn.coder.spring.bean.BeansException;

public interface BeanPostProcessor {

    // 在 Bean 对象执行初始化方法之前，执行此方法
    Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;

    // Bean 对象执行初始化方法之后，执行
    Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;

}
