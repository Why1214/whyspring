package org.whyspring.test.v5;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.whyspring.context.ApplicationContext;
import org.whyspring.context.support.ClassPathXmlApplicationContext;
import org.whyspring.service.v5.PetStoreService;
import org.whyspring.util.MessageTracker;

import java.util.List;

public class ApplicationContextTest5 {

    @Before
    public void setUp() {
        MessageTracker.clearMsgs();
    }

    @Test
    public void testPlaceOrder() {

        ApplicationContext ctx = new ClassPathXmlApplicationContext("petstore-v5.xml");
        PetStoreService petStore = (PetStoreService) ctx.getBean("petStore");

        Assert.assertNotNull(petStore.getAccountDao());
        Assert.assertNotNull(petStore.getItemDao());

        petStore.placeOrder();

        List<String> msgs = MessageTracker.getMsgs();

        Assert.assertEquals(3, msgs.size());
        Assert.assertEquals("start tx", msgs.get(0));
        Assert.assertEquals("place order", msgs.get(1));
        Assert.assertEquals("commit tx", msgs.get(2));

    }
}
