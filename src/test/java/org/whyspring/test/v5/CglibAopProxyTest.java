package org.whyspring.test.v5;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.whyspring.aop.aspectj.AspectJAfterReturningAdvice;
import org.whyspring.aop.aspectj.AspectJBeforeAdvice;
import org.whyspring.aop.aspectj.AspectJExpressionPointcut;
import org.whyspring.aop.config.AspectInstanceFactory;
import org.whyspring.aop.framework.AopConfig;
import org.whyspring.aop.framework.AopConfigSupport;
import org.whyspring.aop.framework.CglibProxyFactory;
import org.whyspring.beans.factory.BeanFactory;
import org.whyspring.service.v5.PetStoreService;
import org.whyspring.util.MessageTracker;

import java.util.List;

public class CglibAopProxyTest extends AbstractV5Test {

    private AspectJBeforeAdvice beforeAdvice = null;
    private AspectJAfterReturningAdvice afterAdvice = null;
    private AspectJExpressionPointcut pc = null;
    private BeanFactory beanFactory = null;
    private AspectInstanceFactory aspectInstanceFactory = null;

    @Before
    public void setUp() throws Exception {

        MessageTracker.clearMsgs();

        String expression = "execution(* org.whyspring.service.v5.*.placeOrder(..))";
        pc = new AspectJExpressionPointcut();
        pc.setExpression(expression);

        beanFactory = this.getBeanFactory("petstore-v5.xml");
        aspectInstanceFactory = this.getAspectInstanceFactory("tx");
        aspectInstanceFactory.setBeanFactory(beanFactory);

        beforeAdvice = new AspectJBeforeAdvice(
                getAdviceMethod("start"),
                pc,
                aspectInstanceFactory);

        afterAdvice = new AspectJAfterReturningAdvice(
                getAdviceMethod("commit"),
                pc,
                aspectInstanceFactory);

    }

    @Test
    public void testGetProxy() {

        AopConfig config = new AopConfigSupport();

        config.addAdvice(beforeAdvice);
        config.addAdvice(afterAdvice);
        config.setTargetObject(new PetStoreService());


        CglibProxyFactory proxyFactory = new CglibProxyFactory(config);

        PetStoreService proxy = (PetStoreService) proxyFactory.getProxy();

        proxy.placeOrder();


        List<String> msgs = MessageTracker.getMsgs();
        Assert.assertEquals(3, msgs.size());
        Assert.assertEquals("start tx", msgs.get(0));
        Assert.assertEquals("place order", msgs.get(1));
        Assert.assertEquals("commit tx", msgs.get(2));

        proxy.toString();
    }


}
