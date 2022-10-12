package cn.coder.spring.context;

import cn.coder.spring.bean.BeansException;
import cn.coder.spring.bean.factory.Aware;

public interface ApplicationContextAware extends Aware {

    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;

}
