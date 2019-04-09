package org.whyspring.aop;

import java.lang.reflect.Method;

public interface MethodMatcher {

    // 判断方法是否和表达式匹配
    boolean matches(Method method/*, Class<?> targetClass*/);
}
