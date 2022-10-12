package cn.coder.spring.bean.factory;

public interface DisposableBean {

    void destroy() throws Exception;

}
