package cn.coder.spring.context;

import cn.coder.spring.bean.factory.HierarchicalBeanFactory;
import cn.coder.spring.bean.factory.ListableBeanFactory;
import cn.coder.spring.core.io.ResourceLoader;

/**
 * @author coder
 */
public interface ApplicationContext extends ListableBeanFactory, HierarchicalBeanFactory, ResourceLoader, ApplicationEventPublisher {
}
