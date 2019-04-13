package org.whyspring.test.v5;

import org.junit.Assert;
import org.junit.Test;
import org.whyspring.aop.Advice;
import org.whyspring.aop.aspectj.AspectJAfterReturningAdvice;
import org.whyspring.aop.aspectj.AspectJAfterThrowingAdvice;
import org.whyspring.aop.aspectj.AspectJBeforeAdvice;
import org.whyspring.beans.factory.BeanFactory;
import org.whyspring.tx.TransactionManager;

import java.util.List;

public class BeanFactoryTestV5 extends AbstractV5Test {

    static String expectedExpression = "execution(* org.whyspring.service.v5.*.placeOrder(..))";

    @Test
    public void testGetBeanByType() throws Exception {

        BeanFactory factory = super.getBeanFactory("petstore-v5.xml");

        List<Object> advices = factory.getBeansByType(Advice.class);

        Assert.assertEquals(3, advices.size());

        {
            AspectJBeforeAdvice advice = (AspectJBeforeAdvice) this.getAdvice(AspectJBeforeAdvice.class, advices);

            Assert.assertEquals(TransactionManager.class.getMethod("start"), advice.getAdviceMethod());

            Assert.assertEquals(expectedExpression, advice.getPointcut().getExpression());

            Assert.assertEquals(TransactionManager.class, advice.getAdviceInstance().getClass());

        }


        {
            AspectJAfterReturningAdvice advice = (AspectJAfterReturningAdvice) this.getAdvice(AspectJAfterReturningAdvice.class, advices);

            Assert.assertEquals(TransactionManager.class.getMethod("commit"), advice.getAdviceMethod());

            Assert.assertEquals(expectedExpression, advice.getPointcut().getExpression());

            Assert.assertEquals(TransactionManager.class, advice.getAdviceInstance().getClass());

        }

        {
            AspectJAfterThrowingAdvice advice = (AspectJAfterThrowingAdvice) this.getAdvice(AspectJAfterThrowingAdvice.class, advices);

            Assert.assertEquals(TransactionManager.class.getMethod("rollback"), advice.getAdviceMethod());

            Assert.assertEquals(expectedExpression, advice.getPointcut().getExpression());

            Assert.assertEquals(TransactionManager.class, advice.getAdviceInstance().getClass());

        }


    }

    public Object getAdvice(Class<?> type, List<Object> advices) {
        for (Object o : advices) {
            if (o.getClass().equals(type)) {
                return o;
            }
        }
        return null;
    }
}
