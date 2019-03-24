package org.whyspring.test.v1;

import org.junit.Assert;
import org.junit.Test;
import org.whyspring.beans.BeanDefinition;
import org.whyspring.beans.factory.BeanCreationException;
import org.whyspring.beans.factory.BeanDefinitionStoreException;
import org.whyspring.beans.factory.BeanFactory;
import org.whyspring.beans.factory.support.DefaultBeanFactory;
import org.whyspring.service.v1.PetStoreService;

public class BeanFactoryTest {

    @Test
    public void testGetBean() {
        BeanFactory factory = new DefaultBeanFactory("petstore-v1.xml");
        BeanDefinition bd = factory.getBeanDefinition("petStore");
        Assert.assertEquals("org.whyspring.service.v1.PetStoreService", bd.getBeanClassName());
        PetStoreService petStoreService = (PetStoreService) factory.getBean("petStore");
        Assert.assertNotNull(petStoreService);
    }

    @Test
    public void testInvalidBean() {
        BeanFactory factory = new DefaultBeanFactory("petstore-v1.xml");

        try {
            factory.getBean("invalidBean");
        } catch (BeanCreationException e) {
            return;
        }

        Assert.fail("except BeanCreationException");
    }

    @Test
    public void testInvalidXML() {

        try {
            new DefaultBeanFactory("xxx-v1.xml");
        } catch (BeanDefinitionStoreException e) {
            return;
        }

        Assert.fail("except BeanDefinitionStoreException");
    }
}
