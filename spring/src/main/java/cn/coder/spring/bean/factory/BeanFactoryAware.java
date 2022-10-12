package cn.coder.spring.bean.factory;

import cn.coder.spring.bean.BeansException;

public interface BeanFactoryAware extends Aware {

    void setBeanFactory(BeanFactory beanFactory) throws BeansException;

}
