package org.whyspring.beans.factory.support;

import org.whyspring.beans.BeanDefinition;
import org.whyspring.beans.factory.BeanCreationException;
import org.whyspring.beans.factory.config.ConfigurableBeanFactory;

public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements ConfigurableBeanFactory {
    protected abstract Object createBean(BeanDefinition bd) throws BeanCreationException;
}
