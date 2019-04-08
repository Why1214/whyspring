package org.whyspring.beans.factory.xml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.whyspring.beans.BeanDefinition;
import org.whyspring.beans.ConstructorArgument;
import org.whyspring.beans.PropertyValue;
import org.whyspring.beans.factory.BeanDefinitionStoreException;
import org.whyspring.beans.factory.config.RuntimeBeanReference;
import org.whyspring.beans.factory.config.TypedStringValue;
import org.whyspring.beans.factory.support.BeanDefinitionRegistry;
import org.whyspring.beans.factory.support.GenericBeanDefinition;
import org.whyspring.context.annotation.ClassPathBeanDefinitionScanner;
import org.whyspring.core.io.Resource;
import org.whyspring.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class XmlBeanDefinitionReader {

    private static final String ID_ATTRIBUTE = "id";
    private static final String CLASS_ATTRIBUTE = "class";
    private static final String SCOPE_ATTRIBUTE = "scope";
    private static final String PROPERTY_ELEMENT = "property";
    private static final String REF_ATTRIBUTE = "ref";
    private static final String VALUE_ATTRIBUTE = "value";
    private static final String NAME_ATTRIBUTE = "name";
    public static final String CONSTRUCTOR_ARG_ELEMENT = "constructor-arg";
    public static final String TYPE_ATTRIBUTE = "type";
    public static final String BEANS_NAMESPACE_URI = "http://www.springframework.org/schema/beans";
    public static final String CONTEXT_NAMESPACE_URI = "http://www.springframework.org/schema/context";
    private static final String BASE_PACKAGE_ATTRIBUTE = "base-package";

    private final Log log = LogFactory.getLog(getClass());

    private BeanDefinitionRegistry registry;

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void loadBeanDefinitions(Resource resource) {
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

                if (this.isDefaultNamespace(beanElement.getNamespaceURI())) {
                    parseDefaultElement(beanElement);
                } else if (this.isContextNamespace(beanElement.getNamespaceURI())) {
                    parseComponentElement(beanElement);
                }
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

    private void parseDefaultElement(Element beanElement) {
        String beanId = beanElement.attributeValue(ID_ATTRIBUTE);
        String beanClassName = beanElement.attributeValue(CLASS_ATTRIBUTE);

        // 将bean标签的数据存在一个BeanDefinition中，并将生成的BeanDefinition存在map中
        BeanDefinition bd = new GenericBeanDefinition(beanId, beanClassName);
        if (beanElement.attributeValue(SCOPE_ATTRIBUTE) != null) {
            bd.setScope(beanElement.attributeValue(SCOPE_ATTRIBUTE));
        }

        // 解析constructor-arg标签
        parseConstructorArgElements(beanElement, bd);

        // 解析property标签
        parsePropertyElement(beanElement, bd);

        this.registry.registerBeanDefinition(beanId, bd);
    }

    private void parseComponentElement(Element beanElement) {
        String basePackage = beanElement.attributeValue(BASE_PACKAGE_ATTRIBUTE);
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry);
        scanner.doScan(basePackage);
    }

    private void parseConstructorArgElements(Element beanElem, BeanDefinition bd) {
        Iterator<Element> constructorArgElems = beanElem.elementIterator(CONSTRUCTOR_ARG_ELEMENT);
        while (constructorArgElems.hasNext()) {
            Element constructorArgElem = constructorArgElems.next();
            parseConstructorArgElement(constructorArgElem, bd);
        }
    }

    private void parseConstructorArgElement(Element constructorArgElem, BeanDefinition bd) {
        String nameAttr = constructorArgElem.attributeValue(NAME_ATTRIBUTE);
        String typeAttr = constructorArgElem.attributeValue(TYPE_ATTRIBUTE);

        Object propertyValue = parsePropertyValue(constructorArgElem, bd, null);

        ConstructorArgument.ValueHolder valueHolder = new ConstructorArgument.ValueHolder(propertyValue);

        if (StringUtils.hasLength(typeAttr)) {
            valueHolder.setType(typeAttr);
        }
        if (StringUtils.hasLength(nameAttr)) {
            valueHolder.setName(nameAttr);
        }

        bd.getConstructorArgument().addArgumentValue(valueHolder);
    }

    private void parsePropertyElement(Element beanElem, BeanDefinition bd) {
        Iterator<Element> propertyElems = beanElem.elementIterator(PROPERTY_ELEMENT);
        while (propertyElems.hasNext()) {
            Element propertyElem = propertyElems.next();
            String propertyName = propertyElem.attributeValue(NAME_ATTRIBUTE);
            if (!StringUtils.hasLength(propertyName)) {
                log.fatal("Tag 'property' must have a 'name' attribute");
                return;
            }

            Object value = parsePropertyValue(propertyElem, bd, propertyName);
            PropertyValue propertyValue = new PropertyValue(propertyName, value);
            bd.getPropertyValues().add(propertyValue);
        }
    }

    private Object parsePropertyValue(Element propertyElem, BeanDefinition bd, String propertyName) {
        String elementName = (propertyName != null) ?
                "<property> element for property '" + propertyName + "'" :
                "<constructor-arg> element";

        boolean hasRefAttribute = propertyElem.attributeValue(REF_ATTRIBUTE) != null;
        boolean hasValueAttribute = propertyElem.attributeValue(VALUE_ATTRIBUTE) != null;

        if (hasRefAttribute) {
            String refName = propertyElem.attributeValue(REF_ATTRIBUTE);
            if (!StringUtils.hasText(refName)) {
                log.error(elementName + " contains empty 'ref' attribute");
            }
            RuntimeBeanReference runtimeBeanReference = new RuntimeBeanReference(refName);
            return runtimeBeanReference;
        } else if (hasValueAttribute) {
            String value = propertyElem.attributeValue(VALUE_ATTRIBUTE);
            TypedStringValue typedStringValue = new TypedStringValue(value);
            return typedStringValue;
        } else {
            throw new RuntimeException(elementName + "must specify a ref or value");
        }
    }

    public boolean isDefaultNamespace(String namespaceUri) {
        return (!StringUtils.hasLength(namespaceUri) || BEANS_NAMESPACE_URI.equals(namespaceUri));
    }

    public boolean isContextNamespace(String namespaceUri) {
        return (!StringUtils.hasLength(namespaceUri) || CONTEXT_NAMESPACE_URI.equals(namespaceUri));
    }
}
