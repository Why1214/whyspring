package org.whyspring.test.v4;

import org.junit.Assert;
import org.junit.Test;
import org.whyspring.context.ApplicationContext;
import org.whyspring.context.support.ClassPathXmlApplicationContext;
import org.whyspring.service.v4.PetStoreService;

public class ApplicationContextTest4 {

    @Test
    public void testGetBeanProperty() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("petstore-v4.xml");
        PetStoreService petStore = (PetStoreService)ctx.getBean("petStore");

        Assert.assertNotNull(petStore.getAccountDao());
        Assert.assertNotNull(petStore.getItemDao());

    }
}
