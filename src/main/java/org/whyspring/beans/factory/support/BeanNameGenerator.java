package org.whyspring.beans.factory.support;

import org.whyspring.beans.BeanDefinition;

public interface BeanNameGenerator {
    String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry);
}
