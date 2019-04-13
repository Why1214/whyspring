package org.whyspring.aop.aspectj;

import org.aopalliance.intercept.MethodInvocation;
import org.whyspring.aop.config.AspectInstanceFactory;

import java.lang.reflect.Method;

public class AspectJAfterReturningAdvice extends AbstractAspectJAdvice {

    public AspectJAfterReturningAdvice(Method adviceMethod,
                                       AspectJExpressionPointcut pointcut,
                                       AspectInstanceFactory adviceObjectFactory) {
        super(adviceMethod, pointcut, adviceObjectFactory);
    }


    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        // 递归
        Object o = methodInvocation.proceed();
        // 再调用增强方法
        invokeAdviceMethod();
        return o;
    }
}
