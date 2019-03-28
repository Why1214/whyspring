package org.whyspring.beans.factory.config;

import org.whyspring.beans.factory.BeanFactory;

public interface ConfigurableBeanFactory extends BeanFactory {

    void setBeanClassLoader(ClassLoader beanClassLoader);

    ClassLoader getClassLoader();
}
