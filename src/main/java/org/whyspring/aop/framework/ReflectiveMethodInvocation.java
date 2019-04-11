package org.whyspring.aop.framework;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;

public class ReflectiveMethodInvocation implements MethodInvocation {

    // 目标对象
    protected final Object targetObject;

    // 目标方法
    protected final Method targetMethod;

    // 目标方法的参数
    protected Object[] arguments;

    // 拦截器集合（Advice）
    protected final List<MethodInterceptor> interceptors;

    /**
     * Index from 0 of the current interceptor we're invoking.
     * -1 until we invoke: then the current interceptor.
     */
    private int currentInterceptorIndex = -1;


    public ReflectiveMethodInvocation(
            Object target, Method method, Object[] arguments,
            List<MethodInterceptor> interceptors) {
        this.targetObject = target;
        this.targetMethod = method;
        this.arguments = arguments;
        this.interceptors = interceptors;
    }


    public final Object getThis() {
        return this.targetObject;
    }


    /**
     * Return the method invoked on the proxied interface.
     * May or may not correspond with a method invoked on an underlying
     * implementation of that interface.
     */
    public final Method getMethod() {
        return this.targetMethod;
    }

    public final Object[] getArguments() {
        return (this.arguments != null ? this.arguments : new Object[0]);
    }


    public Object proceed() throws Throwable {
        //	所有的拦截器已经调用完成
        if (this.currentInterceptorIndex == this.interceptors.size() - 1) {
            return invokeJoinpoint();
        }

        this.currentInterceptorIndex++;

        // 递归获取拦截器集合中的拦截器
        MethodInterceptor interceptor =
                this.interceptors.get(this.currentInterceptorIndex);

        // 执行拦截器的方法
        return interceptor.invoke(this);

    }

    /**
     * Invoke the joinpoint using reflection.
     * Subclasses can override this to use custom invocation.
     *
     * @return the return value of the joinpoint
     * @throws Throwable if invoking the joinpoint resulted in an exception
     */
    protected Object invokeJoinpoint() throws Throwable {
        // 调用目标对象的方法
        return this.targetMethod.invoke(this.targetObject, this.arguments);
    }

    public AccessibleObject getStaticPart() {
        return this.targetMethod;
    }
}
