package cn.coder.spring.aop;

public interface PointcutAdvisor extends Advisor {

    Pointcut getPointcut();

}
