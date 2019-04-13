package org.whyspring.beans.factory;


import java.util.List;

public interface BeanFactory {

    Object getBean(String beanId);

    Class<?> getType(String beanName) throws NoSuchBeanDefinitionException;

    List<Object> getBeansByType(Class<?> type);
}
