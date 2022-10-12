package cn.coder.spring.bean.factory.support;

import cn.coder.spring.bean.BeansException;
import cn.coder.spring.bean.factory.FactoryBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry {

    private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<>();

    protected Object getCachedObjectForFactoryBean(String beanName) {
        Object bean = factoryBeanObjectCache.get(beanName);
        return bean != NULL_OBJECT ? bean : null;
    }

    protected Object getObjectFromFactoryBean(FactoryBean factoryBean, String beanName) {
        if (factoryBean.isSingleton()) {
            Object object = this.factoryBeanObjectCache.get(beanName);
            if (object == null) {
                object = doGetObjectFromFactoryBean(factoryBean, beanName);
                object = object != NULL_OBJECT ? object : new Object();
                this.factoryBeanObjectCache.put(beanName, object);
            }
            return object != NULL_OBJECT ? object : null;
        }
        return doGetObjectFromFactoryBean(factoryBean, beanName);
    }

    protected Object doGetObjectFromFactoryBean(final FactoryBean factoryBean, final String beanName) {
        try {
            return factoryBean.getObject();
        } catch (Exception e) {
            throw new BeansException("FactoryBean threw exception on object[" + beanName + "] creation", e);
        }
    }

}
