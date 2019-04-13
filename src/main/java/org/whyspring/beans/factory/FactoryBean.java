package org.whyspring.beans.factory;

/**
 * 每一个类型对象的产生都包含了相同的方法
 * 所以将其抽象成FactoryBean类
 *
 * @param <T>
 */
public interface FactoryBean<T> {

    T getObject() throws Exception;

    Class<?> getObjectType();
}
