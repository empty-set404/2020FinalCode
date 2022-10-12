package cn.coder.spring.context;

public interface ApplicationEventPublisher {

    void publishEvent(ApplicationEvent event);

}
