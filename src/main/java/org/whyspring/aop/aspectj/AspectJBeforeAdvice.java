package org.whyspring.aop.aspectj;

import org.aopalliance.intercept.MethodInvocation;
import org.whyspring.aop.config.AspectInstanceFactory;

import java.lang.reflect.Method;

public class AspectJBeforeAdvice extends AbstractAspectJAdvice {

    public AspectJBeforeAdvice(Method adviceMethod,
                               AspectJExpressionPointcut pointcut,
                               AspectInstanceFactory adviceObjectFactory) {
        super(adviceMethod, pointcut, adviceObjectFactory);
    }

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        // 先调用增强方法
        invokeAdviceMethod();
        // 递归
        Object o = methodInvocation.proceed();
        return o;
    }
}
