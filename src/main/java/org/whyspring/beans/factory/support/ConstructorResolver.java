package org.whyspring.beans.factory.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.whyspring.beans.BeanDefinition;
import org.whyspring.beans.ConstructorArgument;
import org.whyspring.beans.SimpleTypeConverter;
import org.whyspring.beans.factory.BeanCreationException;
import org.whyspring.beans.factory.config.ConfigurableBeanFactory;

import java.lang.reflect.Constructor;
import java.util.List;

public class ConstructorResolver {

    protected final Log logger = LogFactory.getLog(getClass());

    private final ConfigurableBeanFactory beanFactory;

    public ConstructorResolver(ConfigurableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Object autowireConstructor(BeanDefinition bd) {
        Constructor<?> constructorToUse = null;
        Object[] argsToUse = null;
        Class<?> beanClass = null;

        try {
            // 通过beanClassName获取bean的Class对象
            beanClass = this.beanFactory.getBeanClassLoader().loadClass(bd.getBeanClassName());
        } catch (ClassNotFoundException e) {
            throw new BeanCreationException(bd.getBeanId(), "Instantiation of bean failed, can't resolve class", e);
        }

        // 获取构造函数集合
        Constructor<?>[] candidates = beanClass.getConstructors();

        BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this.beanFactory);

        // 解析xml之后的参数集合
        ConstructorArgument cargs = bd.getConstructorArgument();

        SimpleTypeConverter typeConverter = new SimpleTypeConverter();

        for (int i = 0; i < candidates.length; i++) {

            Class<?>[] parameterTypes = candidates[i].getParameterTypes();
            if (parameterTypes.length != cargs.getArgumentCount()) {
                continue;
            }

            argsToUse = new Object[parameterTypes.length];

            boolean result = this.valuesMatchTypes(parameterTypes,
                    cargs.getArgumentValues(),
                    argsToUse,
                    valueResolver,
                    typeConverter);

            if (result) {
                constructorToUse = candidates[i];
                break;
            }
        }

        if (constructorToUse == null) {
            throw new BeanCreationException(bd.getBeanId(), "can't find a apporiate constructor");
        }

        try {
            return constructorToUse.newInstance(argsToUse);
        } catch (Exception e) {
            throw new BeanCreationException(bd.getBeanId(), "can't find a create instance using " + constructorToUse);
        }
    }

    private boolean valuesMatchTypes(Class<?>[] parameterTypes,
                                     List<ConstructorArgument.ValueHolder> valueHolders,
                                     Object[] argsToUse,
                                     BeanDefinitionValueResolver valueResolver,
                                     SimpleTypeConverter typeConverter) {
        for (int i = 0; i < parameterTypes.length; i++) {

            Class<?> parameterType = parameterTypes[i];

            // 获取解析xml中constructor-arg的数据
            ConstructorArgument.ValueHolder valueHolder = valueHolders.get(i);
            Object originalValue = valueHolder.getValue();

            try {
                // 转成真正需要的值
                Object resolvedValue = valueResolver.resolverValueIfNecessary(originalValue);

                // 将值转成构造函数需要的类型
                Object convertedValue = typeConverter.convertIfNecessary(resolvedValue, parameterType);

                argsToUse[i] = convertedValue;
            } catch (Exception e) {
                logger.error(e);
                return false;
            }
        }

        return true;
    }
}
