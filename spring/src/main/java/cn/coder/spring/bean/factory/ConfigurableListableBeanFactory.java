package cn.coder.spring.bean.factory;

import cn.coder.spring.bean.BeansException;
import cn.coder.spring.bean.factory.config.AutowireCapableBeanFactory;
import cn.coder.spring.bean.factory.config.BeanDefinition;
import cn.coder.spring.bean.factory.config.BeanPostProcessor;
import cn.coder.spring.bean.factory.config.ConfigurableBeanFactory;

public interface ConfigurableListableBeanFactory extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory {

    BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    void preInstantiateSingletons() throws BeansException;

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) throws BeansException;

}
