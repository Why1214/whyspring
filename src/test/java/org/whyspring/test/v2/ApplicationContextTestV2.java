package org.whyspring.test.v2;

import org.junit.Assert;
import org.junit.Test;
import org.whyspring.context.ApplicationContext;
import org.whyspring.context.support.ClassPathXmlApplicationContext;
import org.whyspring.dao.v2.AccountDao;
import org.whyspring.dao.v2.ItemDao;
import org.whyspring.service.v2.PetStoreService;

public class ApplicationContextTestV2 {

    @Test
    public void testGetBeanProperty() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("petstore-v2.xml");
        PetStoreService petStoreService = (PetStoreService) ctx.getBean("petStore");
        Assert.assertNotNull(petStoreService.getAccountDao());
        Assert.assertNotNull(petStoreService.getItemDao());

        Assert.assertTrue(petStoreService.getAccountDao() instanceof AccountDao);
        Assert.assertTrue(petStoreService.getItemDao() instanceof ItemDao);

        Assert.assertEquals("why", petStoreService.getOwner());
        Assert.assertEquals(2, petStoreService.getVersion());
    }
}
