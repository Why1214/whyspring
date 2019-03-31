package org.whyspring.beans.factory.support;

import org.whyspring.beans.factory.config.RuntimeBeanReference;
import org.whyspring.beans.factory.config.TypedStringValue;

public class BeanDefinitionValueResolver {

    private final DefaultBeanFactory beanFactory;

    public BeanDefinitionValueResolver(DefaultBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Object resolverValueIfNecessary(Object value) {
        if (value instanceof RuntimeBeanReference) {
            RuntimeBeanReference reference = (RuntimeBeanReference) value;
            String beanName = reference.getBeanName();
            Object bean = this.beanFactory.getBean(beanName);
            return bean;
        } else if (value instanceof TypedStringValue) {
            TypedStringValue stringValue = (TypedStringValue) value;
            return stringValue.getValue();
        } else {
            throw new RuntimeException("the value " + value + " has not implemented");
        }
    }
}
