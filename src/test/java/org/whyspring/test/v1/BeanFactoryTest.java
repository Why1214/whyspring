package org.whyspring.test.v1;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.whyspring.beans.BeanDefinition;
import org.whyspring.beans.factory.BeanCreationException;
import org.whyspring.beans.factory.BeanDefinitionStoreException;
import org.whyspring.beans.factory.support.DefaultBeanFactory;
import org.whyspring.beans.factory.xml.XmlBeanDefinitionReader;
import org.whyspring.core.io.ClassPathResource;
import org.whyspring.core.io.Resource;
import org.whyspring.service.v1.PetStoreService;

public class BeanFactoryTest {

    private DefaultBeanFactory factory = null;

    private XmlBeanDefinitionReader reader = null;

    @Before
    public void setUp(){
        factory = new DefaultBeanFactory();
        reader = new XmlBeanDefinitionReader(factory);
    }

    @Test
    public void testGetBean() {

        Resource resource = new ClassPathResource("petstore-v1.xml");
        reader.loadBeanDefinitions(resource);
        BeanDefinition bd = factory.getBeanDefinition("petStore");

        Assert.assertTrue(bd.isSingleton());
        Assert.assertFalse(bd.isPrototype());
        Assert.assertEquals(bd.SCOPE_DEFAULT,bd.getScope());

        Assert.assertEquals("org.whyspring.service.v1.PetStoreService", bd.getBeanClassName());
        PetStoreService petStoreService = (PetStoreService) factory.getBean("petStore");
        Assert.assertNotNull(petStoreService);

        PetStoreService petStoreService1 = (PetStoreService) factory.getBean("petStore");
        Assert.assertTrue(petStoreService.equals(petStoreService1));
    }

    @Test
    public void testInvalidBean() {

        Resource resource = new ClassPathResource("petstore-v1.xml");
        reader.loadBeanDefinitions(resource);
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
            Resource resource = new ClassPathResource("xxx.xml");
            reader.loadBeanDefinitions(resource);
        } catch (BeanDefinitionStoreException e) {
            return;
        }

        Assert.fail("except BeanDefinitionStoreException");
    }
}
