package org.whyspring.beans.factory.xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.whyspring.beans.BeanDefinition;
import org.whyspring.beans.factory.BeanDefinitionStoreException;
import org.whyspring.beans.factory.support.BeanDefinitionRegistry;
import org.whyspring.beans.factory.support.GenericBeanDefinition;
import org.whyspring.core.io.Resource;
import org.whyspring.util.ClassUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class XmlBeanDefinitionReader {

    private static final String ID_ATTRIBUTE = "id";

    private static final String CLASS_ATTRIBUTE = "class";

    private BeanDefinitionRegistry registry;

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void loadBeanDefinition(Resource resource) {
        InputStream is = null;

        try {
            is = resource.getInputStream();

            // 通过dom4j读取xml文件
            SAXReader reader = new SAXReader();
            Document document = reader.read(is);

            // 获取根标签，即beans标签
            Element rootElement = document.getRootElement();

            // 获取子标签
            Iterator<Element> iter = rootElement.elementIterator();

            // 对子标签进行循环处理
            while (iter.hasNext()) {
                Element beanElement = iter.next();
                String beanId = beanElement.attributeValue(ID_ATTRIBUTE);
                String beanClassName = beanElement.attributeValue(CLASS_ATTRIBUTE);

                // 将bean标签的数据存在一个BeanDefinition中，并将生成的BeanDefinition存在map中
                BeanDefinition bd = new GenericBeanDefinition(beanId, beanClassName);
                this.registry.registerBeanDefinition(beanId, bd);
            }
        } catch (Exception e) {
            throw new BeanDefinitionStoreException("IOException parsing XML document from ", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {

                }
            }
        }
    }

}
