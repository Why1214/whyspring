package org.whyspring.beans.factory.config;

import org.whyspring.beans.factory.BeanFactory;

public interface AutowireCapableBeanFactory extends BeanFactory {
    public Object resolveDependency(DependencyDescriptor descriptor);
}