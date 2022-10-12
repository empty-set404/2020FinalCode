package cn.coder.spring.aop;

import java.lang.reflect.Method;

public interface MethodBeforeAdvice extends BeforeAdvice {

    void before(Method method, Object[] arg, Object target) throws Throwable;

}
