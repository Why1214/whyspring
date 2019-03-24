package org.whyspring.beans.factory.support;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.whyspring.beans.BeanDefinition;
import org.whyspring.beans.factory.BeanCreationException;
import org.whyspring.beans.factory.BeanDefinitionStoreException;
import org.whyspring.beans.factory.BeanFactory;
import org.whyspring.util.ClassUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeanFactory implements BeanFactory {

    private static final String ID_ATTRIBUTE = "id";

    private static final String CLASS_ATTRIBUTE = "class";

    // 存放所有bean的定义
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>(64);

    public DefaultBeanFactory(String configFile) {
        loadBeanDefinition(configFile);
    }

    private void loadBeanDefinition(String configFile) {
        InputStream is = null;

        try {
            ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
            is = classLoader.getResourceAsStream(configFile);

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
                beanDefinitionMap.put(beanId, bd);
            }
        } catch (DocumentException e) {
            throw new BeanDefinitionStoreException("IOException parsing XML document from ",e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {

                }
            }
        }
    }

    public BeanDefinition getBeanDefinition(String beanId) {
        return this.beanDefinitionMap.get(beanId);
    }

    public Object getBean(String beanId) {
        BeanDefinition bd = this.beanDefinitionMap.get(beanId);
        if (bd == null) {
            throw new BeanCreationException("Bean Definition does not exist");
        }

        ClassLoader cl = ClassUtils.getDefaultClassLoader();

        String beanClassName = bd.getBeanClassName();

        try {
            Class<?> clazz = cl.loadClass(beanClassName);

            // 注，此处通过调用无参构造函数
            return clazz.newInstance();
        } catch (Exception e) {
            throw new BeanCreationException("create bean for "+ beanClassName +" failed",e);
        }
    }
}
