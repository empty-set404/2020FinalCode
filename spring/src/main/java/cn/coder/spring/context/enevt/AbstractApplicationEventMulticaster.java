package cn.coder.spring.context.enevt;

import cn.coder.spring.bean.BeansException;
import cn.coder.spring.bean.factory.BeanFactory;
import cn.coder.spring.bean.factory.BeanFactoryAware;
import cn.coder.spring.context.ApplicationEvent;
import cn.coder.spring.context.ApplicationListener;
import cn.coder.spring.util.ClassUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class AbstractApplicationEventMulticaster implements ApplicationEventMulticaster, BeanFactoryAware {

    private final Set<ApplicationListener<ApplicationEvent>> applicationListeners = new LinkedHashSet<>();

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        this.applicationListeners.add((ApplicationListener<ApplicationEvent>) listener);
    }

    @Override
    public void removeApplicationListener(ApplicationListener<?> listener) {
        this.applicationListeners.remove(listener);
    }

    protected Collection<ApplicationListener> getApplicationListeners(ApplicationEvent event) {
        final ArrayList<ApplicationListener> list = new ArrayList<>();
        for (ApplicationListener<ApplicationEvent> applicationListener : this.applicationListeners) {
            if (supportsEvent(applicationListener, event)) {
                list.add(applicationListener);
            }
        }
        return list;
    }

    /**
     * 判断监听器对该事件有兴趣
     */
    protected boolean supportsEvent(ApplicationListener<ApplicationEvent> listener, ApplicationEvent event) {
        Class<? extends ApplicationListener> clazz = listener.getClass();

        // ApplicationListener<?> 中?的真实类型
        Class<?> targetClass = ClassUtils.isCglibProxyClassName(clazz) ? clazz.getSuperclass() : clazz;
        Type genericInterface = targetClass.getGenericInterfaces()[0];
        Type actualTypeArgument = ((ParameterizedType) genericInterface).getActualTypeArguments()[0];
        String eventType = actualTypeArgument.getTypeName();
        Class<?> aClass;
        try {
            aClass = Class.forName(eventType);
        } catch (ClassNotFoundException e) {
            throw new BeansException("wrong event class name: " + eventType);
        }
        // 判定此 eventClassName 对象所表示的类或接口与指定的 event.getClass() 参数所表示的类或接口是否相同，或是否是其超类或超接口。
        return aClass.isAssignableFrom(event.getClass());
    }

}
