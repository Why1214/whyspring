package org.whyspring.beans.factory.support;

import org.whyspring.beans.BeanDefinition;

public class GenericBeanDefinition implements BeanDefinition {

    private String beanId;
    private String beanClassName;

    private boolean isSingleton = true;
    private boolean isPrototype = false;
    private String scope = SCOPE_DEFAULT;

    public GenericBeanDefinition(String beanId, String beanClassName) {
        this.beanId = beanId;
        this.beanClassName = beanClassName;
    }

    public String getBeanClassName() {
        return this.beanClassName;
    }

    public boolean isSingleton() {
        return this.isSingleton;
    }

    public boolean isPrototype() {
        return this.isPrototype;
    }

    public String getScope() {
        return this.scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
        this.isSingleton = (SCOPE_SINGLETON.equals(scope) || SCOPE_DEFAULT.equals(scope));
        this.isPrototype = SCOPE_PROTOTYPE.equals(scope);
    }


}
