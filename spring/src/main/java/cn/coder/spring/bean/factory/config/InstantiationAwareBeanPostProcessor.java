package cn.coder.spring.bean.factory.config;

import cn.coder.spring.bean.BeansException;
import cn.coder.spring.bean.PropertyValues;

public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

    Object postProcessBeforeInstantiation(Class<?> clazz, String name);

    PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException;

}
