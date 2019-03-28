package org.whyspring.context.support;

import org.whyspring.beans.factory.support.DefaultBeanFactory;
import org.whyspring.beans.factory.xml.XmlBeanDefinitionReader;
import org.whyspring.context.ApplicationContext;
import org.whyspring.core.io.Resource;

public abstract class AbstractApplicationContext implements ApplicationContext {

    private DefaultBeanFactory factory;

    public AbstractApplicationContext(String configFile){
        factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        Resource resource = getResourceByPath(configFile);
        reader.loadBeanDefinition(resource);
    }

    public Object getBean(String beanId) {
        return this.factory.getBean(beanId);
    }

    public abstract Resource getResourceByPath(String configFile);
}
