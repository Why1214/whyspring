package org.whyspring.aop.aspectj;

import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

public class AspectJBeforeAdvice extends AbstractAspectJAdvice {

    public AspectJBeforeAdvice(Method adviceMethod, AspectJExpressionPointcut pointcut, Object adviceObject) {
        super(adviceMethod, pointcut, adviceObject);
    }

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        // 先调用增强方法
        invokeAdviceMethod();
        // 递归
        Object o = methodInvocation.proceed();
        return o;
    }
}
