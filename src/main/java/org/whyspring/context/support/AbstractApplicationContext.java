package org.whyspring.context.support;

import org.whyspring.beans.factory.support.DefaultBeanFactory;
import org.whyspring.beans.factory.xml.XmlBeanDefinitionReader;
import org.whyspring.context.ApplicationContext;
import org.whyspring.core.io.Resource;
import org.whyspring.util.ClassUtils;

public abstract class AbstractApplicationContext implements ApplicationContext {

    private DefaultBeanFactory factory;

    private ClassLoader beanClassLoader;

    public AbstractApplicationContext(String configFile) {
        factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        Resource resource = getResourceByPath(configFile);
        reader.loadBeanDefinition(resource);
        factory.setBeanClassLoader(this.getBeanClassLoader());
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
}
