package cn.coder.spring.bean.factory;

import cn.coder.spring.bean.BeansException;
import cn.coder.spring.bean.PropertyValue;
import cn.coder.spring.bean.PropertyValues;
import cn.coder.spring.bean.factory.config.BeanDefinition;
import cn.coder.spring.bean.factory.config.BeanFactoryPostProcessor;
import cn.coder.spring.core.io.DefaultResourceLoader;
import cn.coder.spring.core.io.Resource;
import cn.coder.spring.util.StringValueResolver;

import java.util.Properties;

public class PropertyPlaceholderConfigurer implements BeanFactoryPostProcessor {

    private static final String DEFAULT_PLACEHOLDER_PREFIX = "${";

    private static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";

    private String location;

    /**
     * 加载配置文件 将 ${value}  --> value
     * @param beanFactory
     * @throws BeansException
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        try {
            // 加载属性文件
            DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = resourceLoader.getResource(location);
            Properties properties = new Properties();
            properties.load(resource.getInputStream());

            String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
            for (String beanName : beanDefinitionNames) {
                BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
                PropertyValues propertyValues1 = beanDefinition.getPropertyValues();
                PropertyValue[] propertyValues = propertyValues1.getPropertyValues();
                for (PropertyValue propertyValue : propertyValues) {
                    Object value = propertyValue.getValue();
                    if (!(value instanceof String)) return;
                    StringBuilder sb = new StringBuilder((String)value);
                    int startIndex = ((String) value).indexOf(DEFAULT_PLACEHOLDER_PREFIX);
                    int endIndex = ((String) value).indexOf(DEFAULT_PLACEHOLDER_SUFFIX);
                    if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
                        String propKey = ((String) value).substring(startIndex + 2, endIndex);
                        String proValue = properties.getProperty(propKey);
                        sb.replace(startIndex, endIndex + 1, proValue);
                        propertyValues1.addPropertyValue(new PropertyValue(propertyValue.getName(), sb.toString()));
                    }
                }
            }

            // 向容器中添加字符串解析器，供解析@Value注解使用
            StringValueResolver valueResolver = new PlaceholderResolvingStringValueResolver(properties);
            beanFactory.addEmbeddedValueResolver(valueResolver);

        }catch (Exception e) {
            throw new BeansException("Could not load properties", e);
        }
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private String resolvePlaceholder(String value, Properties properties) {
        StringBuilder buffer = new StringBuilder(value);
        int startIndex = buffer.indexOf(DEFAULT_PLACEHOLDER_PREFIX);
        int endIndex = buffer.indexOf(DEFAULT_PLACEHOLDER_SUFFIX);
        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            String proKey = buffer.substring(startIndex + 2, endIndex);
            String proValue = properties.getProperty(proKey);
            buffer.replace(startIndex, endIndex + 1, proValue);
        }
        return buffer.toString();
    }

    private class PlaceholderResolvingStringValueResolver implements StringValueResolver {

        private final Properties properties;

        private PlaceholderResolvingStringValueResolver(Properties properties) {
            this.properties = properties;
        }

        @Override
        public String resolveStringValue(String strVal) {
            return PropertyPlaceholderConfigurer.this.resolvePlaceholder(strVal, properties);
        }
    }

}
