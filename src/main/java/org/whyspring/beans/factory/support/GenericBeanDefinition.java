package org.whyspring.beans.factory.support;

import org.whyspring.beans.BeanDefinition;
import org.whyspring.beans.ConstructorArgument;
import org.whyspring.beans.PropertyValue;

import java.util.ArrayList;
import java.util.List;

public class GenericBeanDefinition implements BeanDefinition {

    private String beanId;
    private String beanClassName;
    private Class<?> beanClass;
    private boolean isSingleton = true;
    private boolean isPrototype = false;
    private String scope = SCOPE_DEFAULT;

    private List<PropertyValue> propertyValues = new ArrayList<PropertyValue>();
    private ConstructorArgument constructorArgument = new ConstructorArgument();

    public GenericBeanDefinition() {
    }

    public GenericBeanDefinition(String beanId, String beanClassName) {
        this.beanId = beanId;
        this.beanClassName = beanClassName;
    }

    public String getBeanClassName() {
        return this.beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public Class<?> resolveBeanClass(ClassLoader classLoader) throws ClassNotFoundException {
        String className = getBeanClassName();
        if (className == null) {
            return null;
        }
        Class<?> resolvedClass = classLoader.loadClass(className);
        this.beanClass = resolvedClass;
        return resolvedClass;
    }

    public Class<?> getBeanClass() throws IllegalStateException {
        if (this.beanClass == null) {
            throw new IllegalStateException(
                    "Bean class name [" + this.getBeanClassName() + "] has not been resolved into an actual Class");
        }
        return this.beanClass;
    }

    public boolean hasBeanClass() {
        return this.beanClass != null;
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


    public List<PropertyValue> getPropertyValues() {
        return this.propertyValues;
    }

    public ConstructorArgument getConstructorArgument() {
        return this.constructorArgument;
    }

    public String getBeanId() {
        return this.beanId;
    }

    public void setBeanId(String beanId) {
        this.beanId = beanId;
    }

    public boolean hasConstructorArgumentValues() {
        return !this.constructorArgument.isEmpty();
    }
}
