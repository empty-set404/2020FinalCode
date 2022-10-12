package cn.coder.spring.bean.factory;

import cn.coder.spring.bean.BeansException;

import java.util.Map;

public interface ListableBeanFactory extends BeanFactory {

    // 按照类型
    <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException;

    String[] getBeanDefinitionNames();

}
