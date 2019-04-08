package org.whyspring.test.v4;

import org.junit.Assert;
import org.junit.Test;
import org.whyspring.beans.BeanDefinition;
import org.whyspring.beans.factory.support.DefaultBeanFactory;
import org.whyspring.beans.factory.xml.XmlBeanDefinitionReader;
import org.whyspring.context.annotation.ScannedGenericBeanDefinition;
import org.whyspring.core.annotation.AnnotationAttributes;
import org.whyspring.core.io.ClassPathResource;
import org.whyspring.core.io.Resource;
import org.whyspring.core.type.AnnotationMetadata;
import org.whyspring.stereotype.Component;

public class XmlBeanDefinitionReaderTest {

    @Test
    public void testParseScanedBean(){

        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        Resource resource = new ClassPathResource("petstore-v4.xml");
        reader.loadBeanDefinitions(resource);
        String annotation = Component.class.getName();

        {
            BeanDefinition bd = factory.getBeanDefinition("petStore");
            Assert.assertTrue(bd instanceof ScannedGenericBeanDefinition);
            ScannedGenericBeanDefinition sbd = (ScannedGenericBeanDefinition)bd;
            AnnotationMetadata amd = sbd.getMetadata();


            Assert.assertTrue(amd.hasAnnotation(annotation));
            AnnotationAttributes attributes = amd.getAnnotationAttributes(annotation);
            Assert.assertEquals("petStore", attributes.get("value"));
        }
        {
            BeanDefinition bd = factory.getBeanDefinition("accountDao");
            Assert.assertTrue(bd instanceof ScannedGenericBeanDefinition);
            ScannedGenericBeanDefinition sbd = (ScannedGenericBeanDefinition)bd;
            AnnotationMetadata amd = sbd.getMetadata();
            Assert.assertTrue(amd.hasAnnotation(annotation));
        }
        {
            BeanDefinition bd = factory.getBeanDefinition("itemDao");
            Assert.assertTrue(bd instanceof ScannedGenericBeanDefinition);
            ScannedGenericBeanDefinition sbd = (ScannedGenericBeanDefinition)bd;
            AnnotationMetadata amd = sbd.getMetadata();
            Assert.assertTrue(amd.hasAnnotation(annotation));
        }
    }
}
