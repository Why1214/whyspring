package org.whyspring.beans.factory;

import org.whyspring.beans.BeanDefinition;

public interface BeanFactory {

    BeanDefinition getBeanDefinition(String beanId);

    Object getBean(String beanId);
}
