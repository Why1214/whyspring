package org.whyspring.aop.aspectj;

import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

public class AspectJAfterReturningAdvice extends AbstractAspectJAdvice {

    public AspectJAfterReturningAdvice(Method adviceMethod, AspectJExpressionPointcut pointcut, Object adviceObject) {
        super(adviceMethod, pointcut, adviceObject);
    }


    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        // 递归
        Object o = methodInvocation.proceed();
        // 再调用增强方法
        invokeAdviceMethod();
        return o;
    }
}
