package cn.coder.spring.context.annotation;

import cn.coder.spring.bean.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import cn.coder.spring.bean.factory.config.BeanDefinition;
import cn.coder.spring.bean.factory.support.BeanDefinitionRegistry;
import cn.coder.spring.stereotype.Component;
import cn.hutool.core.util.StrUtil;

import java.util.Set;

public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider {

    private BeanDefinitionRegistry registry;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void doScan(String... basePackages) {
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidateComponents = findCandidateComponents(basePackage);
            for (BeanDefinition beanDefinition : candidateComponents) {
                String beanScope = resolveBeanScope(beanDefinition);
                if (StrUtil.isEmpty(basePackage)) {
                    beanDefinition.setScope(beanScope);
                }
                registry.registerBeanDefinition(determineBeanName(beanDefinition), beanDefinition);
            }
        }

        // 注册处理注解的 BeanPostProcessor（@Autowired、@Value）
        registry.registerBeanDefinition("cn.coder.spring.bean.factory.annotation.AutowiredAnnotationBeanPostProcessor", new BeanDefinition(AutowiredAnnotationBeanPostProcessor.class));
    }

    public String resolveBeanScope(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Scope scope = beanClass.getAnnotation(Scope.class);
        if (scope != null) return scope.value();
        return StrUtil.EMPTY;
    }

    public String determineBeanName(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Component component = beanClass.getAnnotation(Component.class);
        final String value = component.value();
        if (StrUtil.isEmpty(value)) {
            if (beanClass.getInterfaces().length > 0) {
                return StrUtil.lowerFirst(beanClass.getInterfaces()[0].getSimpleName());
            }
            return StrUtil.lowerFirst(beanClass.getSimpleName());
        }
        return value;
    }

}
