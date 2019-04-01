package org.whyspring.context.annotation;

import org.whyspring.beans.factory.annotation.AnnotatedBeanDefinition;
import org.whyspring.beans.factory.support.GenericBeanDefinition;
import org.whyspring.core.type.AnnotationMetadata;

public class ScannedGenericBeanDefinition extends GenericBeanDefinition implements AnnotatedBeanDefinition {

    private final AnnotationMetadata metadata;


    public ScannedGenericBeanDefinition(AnnotationMetadata metadata) {
        super();

        this.metadata = metadata;

        setBeanClassName(this.metadata.getClassName());
    }


    public final AnnotationMetadata getMetadata() {
        return this.metadata;
    }

}