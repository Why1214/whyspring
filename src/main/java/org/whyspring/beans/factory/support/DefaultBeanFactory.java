package org.whyspring.beans.factory.support;

import org.whyspring.beans.BeanDefinition;
import org.whyspring.beans.PropertyValue;
import org.whyspring.beans.SimpleTypeConverter;
import org.whyspring.beans.factory.BeanCreationException;
import org.whyspring.beans.factory.config.ConfigurableBeanFactory;
import org.whyspring.util.ClassUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.List;
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
        //  创建实例
        Object bean = instantiateBean(bd);

        // 设置属性
        populateBean(bd, bean);

        return bean;
    }

    private Object instantiateBean(BeanDefinition bd) {
        if (bd.hasConstructorArgumentValues()) {
            ConstructorResolver resolver = new ConstructorResolver(this);
            return resolver.autowireConstructor(bd);
        } else {
            ClassLoader cl = this.getBeanClassLoader();
            String beanClassName = bd.getBeanClassName();

            try {
                Class<?> clazz = cl.loadClass(beanClassName);
                return clazz.newInstance();
            } catch (Exception e) {
                throw new BeanCreationException("create bean for " + beanClassName + "failed", e);
            }
        }
    }

    private void populateBean(BeanDefinition bd, Object bean) {
        List<PropertyValue> propertyValues = bd.getPropertyValues();
        if (propertyValues == null || propertyValues.size() == 0) {
            return;
        }

        BeanDefinitionValueResolver resolver = new BeanDefinitionValueResolver(this);
        SimpleTypeConverter converter = new SimpleTypeConverter();

        try {
            for (PropertyValue propertyValue : propertyValues) {
                String propertyName = propertyValue.getName();
                Object originalValue = propertyValue.getValue();
                Object resolvedValue = resolver.resolverValueIfNecessary(originalValue);

                BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
                PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

                for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                    if (propertyDescriptor.getName().equals(propertyName)) {
                        Object convertedValue = converter.convertIfNecessary(resolvedValue, propertyDescriptor.getPropertyType());
                        propertyDescriptor.getWriteMethod().invoke(bean, convertedValue);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            throw new BeanCreationException("Failed to obtain BeanInfo for class [" + bd.getBeanClassName() + "]", e);
        }
    }

    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = beanClassLoader;
    }

    public ClassLoader getBeanClassLoader() {
        return (this.beanClassLoader != null ? this.beanClassLoader : ClassUtils.getDefaultClassLoader());
    }
}
