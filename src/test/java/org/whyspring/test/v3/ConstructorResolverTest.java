package org.whyspring.test.v3;

import org.junit.Assert;
import org.junit.Test;
import org.whyspring.beans.BeanDefinition;
import org.whyspring.beans.factory.support.ConstructorResolver;
import org.whyspring.beans.factory.support.DefaultBeanFactory;
import org.whyspring.beans.factory.xml.XmlBeanDefinitionReader;
import org.whyspring.core.io.ClassPathResource;
import org.whyspring.service.v3.PetStoreService;

public class ConstructorResolverTest {

    @Test
    public void testAutowireConstructor(){
        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinitions(new ClassPathResource("petstore-v3.xml"));

        BeanDefinition bd = factory.getBeanDefinition("petStore");

        ConstructorResolver resolver = new ConstructorResolver(factory);

        PetStoreService petStore = (PetStoreService)resolver.autowireConstructor(bd);

        Assert.assertEquals(1, petStore.getVersion());
        Assert.assertNotNull(petStore.getAccountDao());
        Assert.assertNotNull(petStore.getItemDao());
    }
}
