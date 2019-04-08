package org.whyspring.beans.factory.annotation;

import org.whyspring.beans.BeansException;
import org.whyspring.beans.factory.BeanCreationException;
import org.whyspring.beans.factory.config.AutowireCapableBeanFactory;
import org.whyspring.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.whyspring.core.annotation.AnnotationUtils;
import org.whyspring.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

public class AutowiredAnnotationProcessor implements InstantiationAwareBeanPostProcessor {

    // 提供处理依赖的能力
    private AutowireCapableBeanFactory beanFactory;

    private String requiredParameterName = "required";

    private boolean requiredParameterValue = true;

    // 初始化可以处理的注解，此处仅需要 Autowired 注解
    private final Set<Class<? extends Annotation>> autowiredAnnotationTypes =
            new LinkedHashSet<Class<? extends Annotation>>();

    public AutowiredAnnotationProcessor() {
        this.autowiredAnnotationTypes.add(Autowired.class);
    }

    public InjectionMetadata buildAutowiringMetadata(Class<?> clazz) {
        // 存放目标类所依赖的属性信息
        LinkedList<InjectionElement> elements = new LinkedList<InjectionElement>();
        // 目标类
        Class<?> targetClass = clazz;
        do {
            LinkedList<InjectionElement> currElements = new LinkedList<InjectionElement>();
            // 对目标类的所有属性进行循环处理
            for (Field field : targetClass.getDeclaredFields()) {
                // 判断当前属性是否有注解，且注解类已经被初始化过
                Annotation ann = findAutowiredAnnotation(field);
                if (ann != null) {
                    if (Modifier.isStatic(field.getModifiers())) {
                        continue;
                    }
                    // 获取当前注解的required的值
                    boolean required = determineRequiredStatus(ann);
                    // 生成AutowiredFieldElement
                    currElements.add(new AutowiredFieldElement(field, required, beanFactory));
                }
            }
            for (Method method : targetClass.getDeclaredMethods()) {
                //TODO 处理方法注入
            }
            elements.addAll(0, currElements);
            targetClass = targetClass.getSuperclass();
        } while (targetClass != null && targetClass != Object.class);

        // 生成InjectionMetadata
        return new InjectionMetadata(clazz, elements);
    }

    protected boolean determineRequiredStatus(Annotation ann) {
        try {
            Method method = ReflectionUtils.findMethod(ann.annotationType(), this.requiredParameterName);
            if (method == null) {
                // Annotations like @Inject and @Value don't have a method (attribute) named "required"
                // -> default to required status
                return true;
            }
            return (this.requiredParameterValue == (Boolean) ReflectionUtils.invokeMethod(method, ann));
        } catch (Exception ex) {
            // An exception was thrown during reflective invocation of the required attribute
            // -> default to required status
            return true;
        }
    }

    private Annotation findAutowiredAnnotation(AccessibleObject ao) {
        // 判断属性是否有注解修饰，且注解所属的类是否被初始化
        for (Class<? extends Annotation> type : this.autowiredAnnotationTypes) {
            Annotation ann = AnnotationUtils.getAnnotation(ao, type);
            if (ann != null) {
                return ann;
            }
        }
        return null;
    }

    public void setBeanFactory(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Object beforeInitialization(Object bean, String beanName) throws BeansException {
        //do nothing
        return bean;
    }

    public Object afterInitialization(Object bean, String beanName) throws BeansException {
        // do nothing
        return bean;
    }

    public Object beforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    public boolean afterInstantiation(Object bean, String beanName) throws BeansException {
        // do nothing
        return true;
    }

    // 实例化之后，实现AutoWired
    public void postProcessPropertyValues(Object bean, String beanName) throws BeansException {
        InjectionMetadata metadata = buildAutowiringMetadata(bean.getClass());
        try {
            metadata.inject(bean);
        } catch (Throwable ex) {
            throw new BeanCreationException(beanName, "Injection of autowired dependencies failed", ex);
        }
    }
}
