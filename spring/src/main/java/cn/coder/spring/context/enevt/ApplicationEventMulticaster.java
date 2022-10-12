package cn.coder.spring.context.enevt;

import cn.coder.spring.context.ApplicationEvent;
import cn.coder.spring.context.ApplicationListener;

public interface ApplicationEventMulticaster {

    void addApplicationListener(ApplicationListener<?> listener);

    void removeApplicationListener(ApplicationListener<?> listener);

    void multicastEvent(ApplicationEvent event);

}
