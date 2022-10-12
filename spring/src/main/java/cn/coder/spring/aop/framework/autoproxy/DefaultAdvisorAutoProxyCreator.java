package cn.coder.spring.aop.framework.autoproxy;

import cn.coder.spring.aop.*;
import cn.coder.spring.aop.aspectj.AspectJExpressionPointcutAdvisor;
import cn.coder.spring.aop.framework.ProxyFactory;
import cn.coder.spring.bean.BeansException;
import cn.coder.spring.bean.PropertyValues;
import cn.coder.spring.bean.factory.BeanFactory;
import cn.coder.spring.bean.factory.BeanFactoryAware;
import cn.coder.spring.bean.factory.config.InstantiationAwareBeanPostProcessor;
import cn.coder.spring.bean.factory.support.DefaultListableBeanFactory;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;

import java.util.Collection;

public class DefaultAdvisorAutoProxyCreator implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private DefaultListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> clazz, String name) {
        if (isInfrastructureClass(clazz)) return null;

        Collection<AspectJExpressionPointcutAdvisor> values = this.beanFactory.getBeansOfType(AspectJExpressionPointcutAdvisor.class).values();
        for (AspectJExpressionPointcutAdvisor value : values) {
            final ClassFilter filter = value.getPointcut().getClassFilter();
            if (!filter.matches(clazz)) continue;

            AdvisedSupport advisedSupport = new AdvisedSupport();
            TargetSource targetSource = null;
            try {
                targetSource = new TargetSource(clazz.getDeclaredConstructor().newInstance());
            }catch (Exception e) {
                e.printStackTrace();
            }
            advisedSupport.setTargetSource(targetSource);
            advisedSupport.setMethodInterceptor((MethodInterceptor) value.getAdvice());
            advisedSupport.setProxyTargetClass(false);
            advisedSupport.setMethodMatcher(value.getPointcut().getMethodMatcher());

            return new ProxyFactory(advisedSupport).getProxy();
        }
        return null;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        return pvs;
    }

    private boolean isInfrastructureClass(Class<?> clazz) {
        return Advice.class.isAssignableFrom(clazz) || Pointcut.class.isAssignableFrom(clazz) || Advisor.class.isAssignableFrom(clazz);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

}
