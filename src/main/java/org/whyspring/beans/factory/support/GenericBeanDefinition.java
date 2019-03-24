package org.whyspring.beans.factory.support;

import org.whyspring.beans.BeanDefinition;

public class GenericBeanDefinition implements BeanDefinition {

    private String beanId;

    private String beanClassName;

    public GenericBeanDefinition(String beanId, String beanClassName){
        this.beanId = beanId;
        this.beanClassName = beanClassName;
    }

    public String getBeanClassName() {
        return this.beanClassName;
    }
}
