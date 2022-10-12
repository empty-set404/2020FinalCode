package cn.coder.spring.context.enevt;

import cn.coder.spring.context.ApplicationContext;
import cn.coder.spring.context.ApplicationEvent;

public class ApplicationContextEvent extends ApplicationEvent {


    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public ApplicationContextEvent(Object source) {
        super(source);
    }

    public ApplicationContext getApplicationContext() {
        return (ApplicationContext) getSource();
    }

}
