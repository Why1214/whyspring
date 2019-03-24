package org.whyspring.beans.factory.support;

import org.whyspring.beans.BeanDefinition;

public interface BeanDefinitionRegistry {

    BeanDefinition getBeanDefinition(String beanId);

    void registerBeanDefinition(String beanId, BeanDefinition beanDefinition);
}
