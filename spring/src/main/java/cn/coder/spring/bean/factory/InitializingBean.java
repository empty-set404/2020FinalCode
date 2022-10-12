package cn.coder.spring.bean.factory;

import cn.coder.spring.bean.BeansException;

public interface InitializingBean {

    // Bean 处理了属性填充后调用
    void afterPropertiesSet() throws BeansException;

}
