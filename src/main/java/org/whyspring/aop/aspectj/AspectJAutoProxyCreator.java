package org.whyspring.aop.aspectj;

import org.whyspring.aop.Advice;
import org.whyspring.aop.MethodMatcher;
import org.whyspring.aop.Pointcut;
import org.whyspring.aop.framework.AopConfigSupport;
import org.whyspring.aop.framework.AopProxyFactory;
import org.whyspring.aop.framework.CglibProxyFactory;
import org.whyspring.beans.BeansException;
import org.whyspring.beans.factory.config.BeanPostProcessor;
import org.whyspring.beans.factory.config.ConfigurableBeanFactory;
import org.whyspring.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class AspectJAutoProxyCreator implements BeanPostProcessor {
    ConfigurableBeanFactory beanFactory;

    public Object beforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public Object afterInitialization(Object bean, String beanName) throws BeansException {
        //如果这个Bean本身就是Advice及其子类，那就不要再生成动态代理了。
        if (isInfrastructureClass(bean.getClass())) {
            return bean;
        }

        List<Advice> advices = getCandidateAdvices(bean);
        if (advices.isEmpty()) {
            return bean;
        }

        return createProxy(advices, bean);
    }

    private List<Advice> getCandidateAdvices(Object bean) {
        // 拿到所有的Advice
        List<Object> advices = this.beanFactory.getBeansByType(Advice.class);
        // 和bean的方法匹配的Advice集合
        List<Advice> result = new ArrayList<Advice>();
        for (Object o : advices) {
            Pointcut pc = ((Advice) o).getPointcut();
            if (canApply(pc, bean.getClass())) {
                result.add((Advice) o);
            }
        }
        return result;
    }

    protected Object createProxy(List<Advice> advices, Object bean) {
        AopConfigSupport config = new AopConfigSupport();
        for (Advice advice : advices) {
            config.addAdvice(advice);
        }

        Set<Class> targetInterfaces = ClassUtils.getAllInterfacesForClassAsSet(bean.getClass());
        for (Class<?> targetInterface : targetInterfaces) {
            config.addInterface(targetInterface);
        }

        config.setTargetObject(bean);

        AopProxyFactory proxyFactory = null;

        // 判断bean是否有接口，如果有接口，通过jdk实现代理；没有用CGLib生成代理
        if (config.getProxiedInterfaces().length == 0) {
            proxyFactory = new CglibProxyFactory(config);
        } else {
            //TODO 需要实现JDK 代理
            //proxyFactory = new JdkAopProxyFactory(config);
        }

        return proxyFactory.getProxy();
    }

    protected boolean isInfrastructureClass(Class<?> beanClass) {
        boolean retVal = Advice.class.isAssignableFrom(beanClass);
        return retVal;
    }

    public void setBeanFactory(ConfigurableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;

    }

    public static boolean canApply(Pointcut pc, Class<?> targetClass) {
        MethodMatcher methodMatcher = pc.getMethodMatcher();

        Set<Class> classes = new LinkedHashSet<Class>(ClassUtils.getAllInterfacesForClassAsSet(targetClass));
        classes.add(targetClass);
        for (Class<?> clazz : classes) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (methodMatcher.matches(method/*, targetClass*/)) {
                    return true;
                }
            }
        }

        return false;
    }

}