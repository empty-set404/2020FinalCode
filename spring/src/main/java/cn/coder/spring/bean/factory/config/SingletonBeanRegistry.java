package cn.coder.spring.bean.factory.config;

public interface SingletonBeanRegistry {

    public Object getSingleton(String beanName);

    void registerSingleton(String beanName, Object singletonObject);

}
