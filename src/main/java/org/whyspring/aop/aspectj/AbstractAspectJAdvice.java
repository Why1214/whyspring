package org.whyspring.aop.aspectj;

import org.whyspring.aop.Advice;
import org.whyspring.aop.Pointcut;
import org.whyspring.aop.config.AspectInstanceFactory;

import java.lang.reflect.Method;

public abstract class AbstractAspectJAdvice implements Advice {

    // 增强方法
    protected Method adviceMethod;

    protected Pointcut pointcut;
    // 增强类
    protected AspectInstanceFactory adviceObjectFactory;

    public AbstractAspectJAdvice(Method adviceMethod,
                                 AspectJExpressionPointcut pointcut,
                                 AspectInstanceFactory adviceObjectFactory) {
        this.adviceMethod = adviceMethod;
        this.pointcut = pointcut;
        this.adviceObjectFactory = adviceObjectFactory;
    }

    public void invokeAdviceMethod() throws Throwable {
        adviceMethod.invoke(adviceObjectFactory.getAspectInstance());
    }

    public Method getAdviceMethod() {
        return adviceMethod;
    }

    public Pointcut getPointcut() {
        return this.pointcut;
    }

    public Object getAdviceInstance() throws Exception {
        return adviceObjectFactory.getAspectInstance();
    }
}
