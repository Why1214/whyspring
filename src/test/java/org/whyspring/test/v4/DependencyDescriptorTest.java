package org.whyspring.test.v4;

import org.junit.Assert;
import org.junit.Test;
import org.whyspring.beans.factory.config.DependencyDescriptor;
import org.whyspring.beans.factory.support.DefaultBeanFactory;
import org.whyspring.beans.factory.xml.XmlBeanDefinitionReader;
import org.whyspring.core.io.ClassPathResource;
import org.whyspring.core.io.Resource;
import org.whyspring.dao.v4.AccountDao;
import org.whyspring.dao.v4.ItemDao;
import org.whyspring.service.v4.PetStoreService;

import java.lang.reflect.Field;

public class DependencyDescriptorTest {

    @Test
    public void testResolveDependency() throws Exception{

        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        Resource resource = new ClassPathResource("petstore-v4.xml");
        reader.loadBeanDefinitions(resource);

        Field f = PetStoreService.class.getDeclaredField("accountDao");
        DependencyDescriptor descriptor = new DependencyDescriptor(f,true);
        Object o = factory.resolveDependency(descriptor);
        Assert.assertTrue(o instanceof AccountDao);


        Field f2 = PetStoreService.class.getDeclaredField("itemDao");
        DependencyDescriptor descriptor2 = new DependencyDescriptor(f2, true);
        Object o2 = factory.resolveDependency(descriptor2);
        Assert.assertTrue(o2 instanceof ItemDao);
    }
}
