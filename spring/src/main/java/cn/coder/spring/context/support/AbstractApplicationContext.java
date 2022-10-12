package cn.coder.spring.context.support;

import cn.coder.spring.bean.BeansException;
import cn.coder.spring.bean.factory.ConfigurableListableBeanFactory;
import cn.coder.spring.bean.factory.config.BeanFactoryPostProcessor;
import cn.coder.spring.bean.factory.config.BeanPostProcessor;
import cn.coder.spring.context.ApplicationEvent;
import cn.coder.spring.context.ApplicationListener;
import cn.coder.spring.context.ConfigurableApplicationContext;
import cn.coder.spring.context.enevt.ApplicationEventMulticaster;
import cn.coder.spring.context.enevt.ContextClosedEvent;
import cn.coder.spring.context.enevt.ContextRefreshedEvent;
import cn.coder.spring.context.enevt.SimpleApplicationEventMulticaster;
import cn.coder.spring.core.io.DefaultResourceLoader;

import java.util.Map;

public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {

    private static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";

    private ApplicationEventMulticaster applicationEventMulticaster;

    @Override
    public void refresh() throws BeansException {
        // 创建bean工厂, 并加载BeanDefinition
        refreshBeanFactory();

        // 获取bean工厂
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();

        // ApplicationContextAwareProcessor
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));

        // 触发事件
        invokeBeanFactoryPostProcessors(beanFactory);

        // 注册事件
        registerBeanPostProcessors(beanFactory);

        // 初始化事件发布者
        initApplicationEventMulticaster();

        // 提前实例化单例Bean对象
        beanFactory.preInstantiateSingletons();

        // 注册事件监听器
        registerListeners();

        // 发布者完成刷新容器事件
        finishRefresh();

    }

    protected abstract void refreshBeanFactory();

    protected abstract ConfigurableListableBeanFactory getBeanFactory();

    private void initApplicationEventMulticaster() {
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
        beanFactory.registerSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, applicationEventMulticaster);
    }

    private void registerListeners() {
        for (ApplicationListener value : getBeansOfType(ApplicationListener.class).values()) {
            applicationEventMulticaster.addApplicationListener(value);
        }
    }

    private void finishRefresh() {
        publishEvent(new ContextRefreshedEvent(this));
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        applicationEventMulticaster.multicastEvent(event);
    }

    private void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanFactoryPostProcessor> beansOfType = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
        for (BeanFactoryPostProcessor value : beansOfType.values()) {
            value.postProcessBeanFactory(beanFactory);
        }
    }

    private void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanPostProcessor> beanPostProcessorMap = beanFactory.getBeansOfType(BeanPostProcessor.class);
        for (BeanPostProcessor value : beanPostProcessorMap.values()) {
            beanFactory.addBeanPostProcessor(value);
        }
    }

    @Override
    public Object getBean(String name) throws BeansException {
        return getBeanFactory().getBean(name);
    }

    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return getBeanFactory().getBean(name, args);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return getBeanFactory().getBean(name, requiredType);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        return getBeanFactory().getBean(requiredType);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return getBeanFactory().getBeansOfType(type);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }

    @Override
    public void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    @Override
    public void close() {
        // 发布容器关闭事件
        publishEvent(new ContextClosedEvent(this));

        getBeanFactory().destroySingletons();
    }
}
