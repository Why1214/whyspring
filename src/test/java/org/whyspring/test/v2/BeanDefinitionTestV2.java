package org.whyspring.test.v2;

import org.junit.Assert;
import org.junit.Test;
import org.whyspring.beans.BeanDefinition;
import org.whyspring.beans.PropertyValue;
import org.whyspring.beans.factory.config.RuntimeBeanReference;
import org.whyspring.beans.factory.support.DefaultBeanFactory;
import org.whyspring.beans.factory.xml.XmlBeanDefinitionReader;
import org.whyspring.core.io.ClassPathResource;

import java.util.List;

public class BeanDefinitionTestV2 {

    @Test
    public void testGetBeanDefinition() {
        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinition(new ClassPathResource("petstore-v2.xml"));

        BeanDefinition bd = factory.getBeanDefinition("petStore");

        List<PropertyValue> values = bd.getPropertyValues();

        Assert.assertEquals(4, values.size());

        {
            PropertyValue propertyValue = this.getPropertyValue("accountDao", values);
            Object accountDaoValue = propertyValue.getValue();
            Assert.assertTrue(accountDaoValue instanceof RuntimeBeanReference);
        }

        {
            PropertyValue propertyValue = this.getPropertyValue("itemDao", values);
            Object itemDaoValue = propertyValue.getValue();
            Assert.assertTrue(itemDaoValue instanceof RuntimeBeanReference);
        }
    }

    private PropertyValue getPropertyValue(String name, List<PropertyValue> pvs) {
        for (PropertyValue pv : pvs) {
            if (pv.getName().equals(name)) {
                return pv;
            }
        }
        return null;
    }
}
