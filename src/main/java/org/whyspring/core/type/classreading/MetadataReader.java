package org.whyspring.core.type.classreading;

import org.whyspring.core.io.Resource;
import org.whyspring.core.type.AnnotationMetadata;
import org.whyspring.core.type.ClassMetadata;

public interface MetadataReader {

    /**
     * Return the resource reference for the class file.
     */
    Resource getResource();

    /**
     * Read basic class metadata for the underlying class.
     */
    ClassMetadata getClassMetadata();

    /**
     * Read full annotation metadata for the underlying class,
     * including metadata for annotated methods.
     */
    AnnotationMetadata getAnnotationMetadata();
}
