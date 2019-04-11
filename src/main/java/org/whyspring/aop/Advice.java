package org.whyspring.aop;

import org.aopalliance.intercept.MethodInterceptor;

public interface Advice extends MethodInterceptor {

    Pointcut getPointcut();
}
