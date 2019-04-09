package org.whyspring.beans.factory;


public interface BeanFactory {

    Object getBean(String beanId);

    Class<?> getType(String beanName) throws NoSuchBeanDefinitionException;
}
