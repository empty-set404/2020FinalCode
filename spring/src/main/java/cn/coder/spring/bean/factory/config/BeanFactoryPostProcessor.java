package cn.coder.spring.bean.factory.config;

import cn.coder.spring.bean.BeansException;
import cn.coder.spring.bean.factory.ConfigurableListableBeanFactory;

public interface BeanFactoryPostProcessor {

    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;

}
