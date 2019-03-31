package org.whyspring.beans.factory.support;

import org.whyspring.beans.BeanDefinition;
import org.whyspring.beans.factory.BeanCreationException;
import org.whyspring.beans.factory.config.ConfigurableBeanFactory;
import org.whyspring.util.ClassUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeanFactory extends DefaultSingletonBeanRegistry
        implements ConfigurableBeanFactory, BeanDefinitionRegistry {

    // 存放所有bean的定义
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>(64);

    private ClassLoader beanClassLoader;

    public DefaultBeanFactory() {

    }

    public void registerBeanDefinition(String beanId, BeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(beanId, beanDefinition);
    }

    public BeanDefinition getBeanDefinition(String beanId) {
        return this.beanDefinitionMap.get(beanId);
    }

    public Object getBean(String beanId) {
        BeanDefinition bd = this.beanDefinitionMap.get(beanId);
        if (bd == null) {
            throw new BeanCreationException("Bean Definition does not exist");
        }

        if (bd.isSingleton()) {
            Object bean = this.getSingleton(bd.getBeanClassName());
            if (bean == null) {
                bean = createBean(bd);
                this.registerSingleton(bd.getBeanClassName(), bean);
            }
            return bean;
        }
        return createBean(bd);
    }

    private Object createBean(BeanDefinition bd) {
        ClassLoader cl = this.getClassLoader();
        String beanClassName = bd.getBeanClassName();

        try {
            Class<?> clazz = cl.loadClass(beanClassName);
            return clazz.newInstance();
        } catch (Exception e) {
            throw new BeanCreationException("create bean for " + beanClassName + "failed", e);
        }
    }

    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = beanClassLoader;
    }

    public ClassLoader getClassLoader() {
        return (this.beanClassLoader != null ? this.beanClassLoader : ClassUtils.getDefaultClassLoader());
    }
}
