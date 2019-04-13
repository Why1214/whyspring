package org.whyspring.test.v2;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.whyspring.beans.factory.config.RuntimeBeanReference;
import org.whyspring.beans.factory.config.TypedStringValue;
import org.whyspring.beans.factory.support.BeanDefinitionValueResolver;
import org.whyspring.beans.factory.support.DefaultBeanFactory;
import org.whyspring.beans.factory.xml.XmlBeanDefinitionReader;
import org.whyspring.core.io.ClassPathResource;
import org.whyspring.dao.v2.AccountDao;

public class BeanDefinitionValueResolverTest {

    private BeanDefinitionValueResolver resolver = null;

    @Before
    public void setUp(){
        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinitions(new ClassPathResource("petstore-v2.xml"));

        resolver = new BeanDefinitionValueResolver(factory);
    }

    @Test
    public void testResolveRuntimeBeanReference() {
        RuntimeBeanReference reference = new RuntimeBeanReference("accountDao");
        Object value = resolver.resolveValueIfNecessary(reference);

        Assert.assertNotNull(value);
        Assert.assertTrue(value instanceof AccountDao);
    }

    @Test
    public void testResolveTypedStringValue() {
        TypedStringValue stringValue = new TypedStringValue("test");
        Object value = resolver.resolveValueIfNecessary(stringValue);

        Assert.assertNotNull(value);
        Assert.assertEquals("test", value);
    }
}
