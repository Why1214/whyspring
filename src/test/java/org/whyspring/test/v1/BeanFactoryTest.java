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
        reader.loadBeanDefinition(resource);
        BeanDefinition bd = factory.getBeanDefinition("petStore");
        Assert.assertEquals("org.whyspring.service.v1.PetStoreService", bd.getBeanClassName());
        PetStoreService petStoreService = (PetStoreService) factory.getBean("petStore");
        Assert.assertNotNull(petStoreService);
    }

    @Test
    public void testInvalidBean() {

        Resource resource = new ClassPathResource("petstore-v1.xml");
        reader.loadBeanDefinition(resource);
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
            reader.loadBeanDefinition(resource);
        } catch (BeanDefinitionStoreException e) {
            return;
        }

        Assert.fail("except BeanDefinitionStoreException");
    }
}
