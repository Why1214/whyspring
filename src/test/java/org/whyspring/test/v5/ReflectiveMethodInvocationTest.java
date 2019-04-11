package org.whyspring.test.v5;

import org.aopalliance.intercept.MethodInterceptor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.whyspring.aop.aspectj.AspectJAfterReturningAdvice;
import org.whyspring.aop.aspectj.AspectJAfterThrowingAdvice;
import org.whyspring.aop.aspectj.AspectJBeforeAdvice;
import org.whyspring.aop.framework.ReflectiveMethodInvocation;
import org.whyspring.service.v5.PetStoreService;
import org.whyspring.tx.TransactionManager;
import org.whyspring.util.MessageTracker;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectiveMethodInvocationTest {

    // 前置增强
    private AspectJBeforeAdvice beforeAdvice = null;
    // 后置增强
    private AspectJAfterReturningAdvice afterAdvice = null;
    // 异常增强
    private AspectJAfterThrowingAdvice afterThrowingAdvice = null;
    // 增强目标类
    private PetStoreService petStoreService = null;
    // 增强的行为类
    private TransactionManager tx;


    @Before
    public void setUp() throws Exception {
        petStoreService = new PetStoreService();
        tx = new TransactionManager();

        MessageTracker.clearMsgs();
        beforeAdvice = new AspectJBeforeAdvice(
                TransactionManager.class.getMethod("start"),
                null,
                tx);

        afterAdvice = new AspectJAfterReturningAdvice(
                TransactionManager.class.getMethod("commit"),
                null,
                tx);

        afterThrowingAdvice = new AspectJAfterThrowingAdvice(
                TransactionManager.class.getMethod("rollback"),
                null,
                tx
        );

    }


    @Test
    public void testMethodInvocation() throws Throwable {
        // 获取目标增强方法的Method对象
        Method targetMethod = PetStoreService.class.getMethod("placeOrder");
        // 增强列表
        List<MethodInterceptor> interceptors = new ArrayList<MethodInterceptor>();
        interceptors.add(beforeAdvice);
        interceptors.add(afterAdvice);

        ReflectiveMethodInvocation mi = new ReflectiveMethodInvocation(petStoreService, targetMethod, new Object[0], interceptors);
        mi.proceed();

        List<String> msgs = MessageTracker.getMsgs();
        Assert.assertEquals(3, msgs.size());
        Assert.assertEquals("start tx", msgs.get(0));
        Assert.assertEquals("place order", msgs.get(1));
        Assert.assertEquals("commit tx", msgs.get(2));

    }

    @Test
    public void testMethodInvocation2() throws Throwable {

        Method targetMethod = PetStoreService.class.getMethod("placeOrder");

        List<MethodInterceptor> interceptors = new ArrayList<MethodInterceptor>();
        interceptors.add(afterAdvice);
        interceptors.add(beforeAdvice);

        ReflectiveMethodInvocation mi = new ReflectiveMethodInvocation(petStoreService, targetMethod, new Object[0], interceptors);
        mi.proceed();

        List<String> msgs = MessageTracker.getMsgs();
        Assert.assertEquals(3, msgs.size());
        Assert.assertEquals("start tx", msgs.get(0));
        Assert.assertEquals("place order", msgs.get(1));
        Assert.assertEquals("commit tx", msgs.get(2));

    }

    @Test
    public void testAfterThrowing() throws Throwable {

        Method targetMethod = PetStoreService.class.getMethod("placeOrderWithException");

        List<MethodInterceptor> interceptors = new ArrayList<MethodInterceptor>();
        interceptors.add(afterThrowingAdvice);
        interceptors.add(beforeAdvice);

        ReflectiveMethodInvocation mi = new ReflectiveMethodInvocation(petStoreService, targetMethod, new Object[0], interceptors);
        try {
            mi.proceed();
        } catch (Throwable t) {
            List<String> msgs = MessageTracker.getMsgs();
            Assert.assertEquals(2, msgs.size());
            Assert.assertEquals("start tx", msgs.get(0));
            Assert.assertEquals("rollback tx", msgs.get(1));
            return;
        }

        Assert.fail("No Exception thrown");
    }
}
