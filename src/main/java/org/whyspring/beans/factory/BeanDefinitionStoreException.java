package org.whyspring.beans.factory;

import org.whyspring.beans.BeansException;

public class BeanDefinitionStoreException extends BeansException {

    public BeanDefinitionStoreException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
