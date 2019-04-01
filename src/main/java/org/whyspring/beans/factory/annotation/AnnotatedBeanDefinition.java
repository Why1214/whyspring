package org.whyspring.beans.factory.annotation;

import org.whyspring.beans.BeanDefinition;
import org.whyspring.core.type.AnnotationMetadata;

public interface AnnotatedBeanDefinition extends BeanDefinition {
    AnnotationMetadata getMetadata();
}
