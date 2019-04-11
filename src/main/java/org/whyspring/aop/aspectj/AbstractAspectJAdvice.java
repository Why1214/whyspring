package org.whyspring.aop.aspectj;

import org.aopalliance.intercept.MethodInvocation;
import org.whyspring.aop.Advice;
import org.whyspring.aop.Pointcut;

import java.lang.reflect.Method;

public abstract class AbstractAspectJAdvice implements Advice {

    // 增强方法
    protected Method adviceMethod;

    protected Pointcut pointcut;
    // 增强类
    protected Object adviceObject;

    public AbstractAspectJAdvice(Method adviceMethod, AspectJExpressionPointcut pointcut, Object adviceObject) {
        this.adviceMethod = adviceMethod;
        this.pointcut = pointcut;
        this.adviceObject = adviceObject;
    }

    public void invokeAdviceMethod() throws Throwable {
        adviceMethod.invoke(adviceObject);
    }

    public Method getAdviceMethod() {
        return adviceMethod;
    }

    public Pointcut getPointcut() {
        return this.pointcut;
    }
}
