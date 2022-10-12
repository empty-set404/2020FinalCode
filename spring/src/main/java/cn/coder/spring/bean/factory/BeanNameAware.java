package cn.coder.spring.bean.factory;

public interface BeanNameAware extends Aware {

    void setBeanName(String beanName);

}
