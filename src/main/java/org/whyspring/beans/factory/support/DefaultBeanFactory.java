package org.whyspring.beans.factory.support;

import org.whyspring.beans.BeanDefinition;
import org.whyspring.beans.BeansException;
import org.whyspring.beans.PropertyValue;
import org.whyspring.beans.SimpleTypeConverter;
import org.whyspring.beans.factory.BeanCreationException;
import org.whyspring.beans.factory.BeanFactoryAware;
import org.whyspring.beans.factory.NoSuchBeanDefinitionException;
import org.whyspring.beans.factory.config.BeanPostProcessor;
import org.whyspring.beans.factory.config.DependencyDescriptor;
import org.whyspring.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.whyspring.util.ClassUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeanFactory extends AbstractBeanFactory
        implements BeanDefinitionRegistry {

    private List<BeanPostProcessor> beanPostProcessors = new ArrayList<BeanPostProcessor>();

    // 存放所有bean的定义
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>(64);

    private ClassLoader beanClassLoader;

    public DefaultBeanFactory() {

    }

    public void addBeanPostProcessor(BeanPostProcessor postProcessor) {
        this.beanPostProcessors.add(postProcessor);
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }

    public void registerBeanDefinition(String beanId, BeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(beanId, beanDefinition);
    }

    public BeanDefinition getBeanDefinition(String beanId) {
        return this.beanDefinitionMap.get(beanId);
    }

    public List<Object> getBeansByType(Class<?> type) {
        List<Object> beans = new ArrayList<Object>();
        List<String> beanIds = this.getBeanIdsByType(type);
        for (String beanId : beanIds) {
            beans.add(this.getBean(beanId));
        }
        return beans;
    }

    private List<String> getBeanIdsByType(Class<?> type) {
        List<String> beanIds = new ArrayList<String>();
        for (String beanId : this.beanDefinitionMap.keySet()) {
            if (type.isAssignableFrom(this.getType(beanId))) {
                beanIds.add(beanId);
            }
        }
        return beanIds;
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

    protected Object createBean(BeanDefinition bd) {
        //  创建实例
        Object bean = instantiateBean(bd);

        // 设置属性
        populateBean(bd, bean);

        bean = initializeBean(bd, bean);

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
        // 处理bean的属性，这些属性都带注解
        for (BeanPostProcessor processor : this.getBeanPostProcessors()) {
            if (processor instanceof InstantiationAwareBeanPostProcessor) {
                ((InstantiationAwareBeanPostProcessor) processor).postProcessPropertyValues(bean, bd.getBeanId());
            }
        }

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
                Object resolvedValue = resolver.resolveValueIfNecessary(originalValue);

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

    protected Object initializeBean(BeanDefinition bd, Object bean) {
        invokeAwareMethods(bean);
        //Todo，调用Bean的init方法，暂不实现
        if (!bd.isSynthetic()) {
            return applyBeanPostProcessorsAfterInitialization(bean, bd.getBeanId());
        }
        return bean;
    }

    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
            throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor beanProcessor : getBeanPostProcessors()) {
            result = beanProcessor.afterInitialization(result, beanName);
            if (result == null) {
                return result;
            }
        }
        return result;
    }

    private void invokeAwareMethods(final Object bean) {
        if (bean instanceof BeanFactoryAware) {
            ((BeanFactoryAware) bean).setBeanFactory(this);
        }
    }

    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = beanClassLoader;
    }

    public ClassLoader getBeanClassLoader() {
        return (this.beanClassLoader != null ? this.beanClassLoader : ClassUtils.getDefaultClassLoader());
    }

    public Object resolveDependency(DependencyDescriptor descriptor) {
        // 根据依赖描述实例拿到属性的class对象
        Class<?> typeToMatch = descriptor.getDependencyType();
        // 循环beandifition集合，判断是否存在与typeToMatch相匹配的beanDifition
        for (BeanDefinition bd : this.beanDefinitionMap.values()) {
            //确保BeanDefinition 有Class对象
            resolveBeanClass(bd);
            Class<?> beanClass = bd.getBeanClass();
            if (typeToMatch.isAssignableFrom(beanClass)) {
                return this.getBean(bd.getBeanId());
            }
        }
        return null;
    }

    public void resolveBeanClass(BeanDefinition bd) {
        if (bd.hasBeanClass()) {
            return;
        } else {
            try {
                bd.resolveBeanClass(this.getBeanClassLoader());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("can't load class:" + bd.getBeanClassName());
            }
        }
    }

    public Class<?> getType(String beanName) throws NoSuchBeanDefinitionException {
        BeanDefinition bd = this.getBeanDefinition(beanName);
        if (bd == null) {
            throw new NoSuchBeanDefinitionException(beanName);
        }
        resolveBeanClass(bd);
        return bd.getBeanClass();
    }
}
