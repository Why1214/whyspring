package org.whyspring.aop.config;

import org.whyspring.beans.factory.BeanFactory;
import org.whyspring.beans.factory.BeanFactoryAware;
import org.whyspring.util.StringUtils;

/**
 * 该类属于切面Aspect标签的工厂类，用来专门生成切面的bean对象
 */
public class AspectInstanceFactory implements BeanFactoryAware {

    private String aspectBeanName;

    private BeanFactory beanFactory;

    public void setAspectBeanName(String aspectBeanName) {
        this.aspectBeanName = aspectBeanName;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        if (!StringUtils.hasText(this.aspectBeanName)) {
            throw new IllegalArgumentException("'aspectBeanName' is required");
        }
    }

    public Object getAspectInstance() throws Exception {
        return this.beanFactory.getBean(this.aspectBeanName);
    }
}
