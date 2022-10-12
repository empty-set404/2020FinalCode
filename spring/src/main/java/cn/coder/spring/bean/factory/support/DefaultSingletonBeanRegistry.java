package cn.coder.spring.bean.factory.support;

import cn.coder.spring.bean.BeansException;
import cn.coder.spring.bean.factory.DisposableBean;
import cn.coder.spring.bean.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    protected final static Object NULL_OBJECT = new Object();

    private Map<String, Object> beanDefinitionMap = new ConcurrentHashMap<String, Object>();

    private Map<String, DisposableBean> disposableBeans = new HashMap<>();

    public Object getSingleton(String beanName) {
        return this.beanDefinitionMap.get(beanName);
    }

    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        this.beanDefinitionMap.put(beanName, singletonObject);
    }

    protected void addSingleton(String name, Object singletonObject) {
        this.beanDefinitionMap.put(name, singletonObject);
    }

    public void registerDisposableBean(String beanName, DisposableBean disposableBean) {
        disposableBeans.put(beanName, disposableBean);
    }

    public void destroySingletons() {
        Set<String> strings = this.disposableBeans.keySet();
        Object[] beanNames = strings.toArray();
        for (int i = beanNames.length - 1; i >= 0; i--) {
            DisposableBean disposableBean = this.disposableBeans.remove(beanNames[i]);
            try {
                disposableBean.destroy();
            } catch (Exception e) {
                throw new BeansException("Destroy method on bean with name '" + beanNames[i] + "' threw an exception", e);
            }
        }
    }

}
