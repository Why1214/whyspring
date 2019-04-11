package org.whyspring.aop.framework;

import org.whyspring.aop.Advice;

import java.lang.reflect.Method;
import java.util.List;

public interface AopConfig {

    Class<?> getTargetClass();

    Object getTargetObject();

    boolean isProxyTargetClass();

    Class<?>[] getProxiedInterfaces();

    boolean isInterfaceProxied(Class<?> intf);

    List<Advice> getAdvices();

    void addAdvice(Advice advice);

    List<Advice> getAdvices(Method method/*,Class<?> targetClass*/);

    void setTargetObject(Object obj);
}
