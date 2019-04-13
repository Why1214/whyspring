package org.whyspring.context.support;

import org.whyspring.aop.aspectj.AspectJAutoProxyCreator;
import org.whyspring.beans.factory.NoSuchBeanDefinitionException;
import org.whyspring.beans.factory.annotation.AutowiredAnnotationProcessor;
import org.whyspring.beans.factory.config.ConfigurableBeanFactory;
import org.whyspring.beans.factory.support.DefaultBeanFactory;
import org.whyspring.beans.factory.xml.XmlBeanDefinitionReader;
import org.whyspring.context.ApplicationContext;
import org.whyspring.core.io.Resource;
import org.whyspring.util.ClassUtils;

import java.util.List;

public abstract class AbstractApplicationContext implements ApplicationContext {

    private DefaultBeanFactory factory;

    private ClassLoader beanClassLoader;

    public AbstractApplicationContext(String configFile) {
        factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        Resource resource = getResourceByPath(configFile);
        reader.loadBeanDefinitions(resource);
        factory.setBeanClassLoader(this.getBeanClassLoader());
        registerBeanPostProcessors(factory);
    }

    public Object getBean(String beanId) {
        return this.factory.getBean(beanId);
    }

    public abstract Resource getResourceByPath(String configFile);

    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.beanClassLoader = beanClassLoader;
    }

    public ClassLoader getBeanClassLoader() {
        return (this.beanClassLoader != null ? this.beanClassLoader : ClassUtils.getDefaultClassLoader());
    }

    protected void registerBeanPostProcessors(ConfigurableBeanFactory beanFactory) {
        AutowiredAnnotationProcessor processor = new AutowiredAnnotationProcessor();
        processor.setBeanFactory(beanFactory);
        factory.addBeanPostProcessor(processor);

        AspectJAutoProxyCreator aspectJAutoProxyCreator = new AspectJAutoProxyCreator();
        aspectJAutoProxyCreator.setBeanFactory(beanFactory);
        factory.addBeanPostProcessor(aspectJAutoProxyCreator);
    }

    public Class<?> getType(String beanName) throws NoSuchBeanDefinitionException {
        return this.factory.getType(beanName);
    }

    public List<Object> getBeansByType(Class<?> type) {
        return this.factory.getBeansByType(type);
    }
}
