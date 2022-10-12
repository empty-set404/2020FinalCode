package cn.coder.spring.bean.factory.support;

import cn.coder.spring.bean.BeansException;
import cn.coder.spring.bean.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;

public interface InstantiationStrategy {

    Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor constructor, Object[] args) throws BeansException;

}
