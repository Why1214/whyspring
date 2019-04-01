package org.whyspring.test.v4;

import org.junit.Assert;
import org.junit.Test;
import org.whyspring.core.annotation.AnnotationAttributes;
import org.whyspring.core.io.ClassPathResource;
import org.whyspring.core.type.AnnotationMetadata;
import org.whyspring.core.type.classreading.MetadataReader;
import org.whyspring.core.type.classreading.SimpleMetadataReader;
import org.whyspring.stereotype.Component;

import java.io.IOException;

public class MetadataReaderTest {

    @Test
    public void testGetMetadata() throws IOException {
        ClassPathResource resource = new ClassPathResource("org/whyspring/service/v4/PetStoreService.class");

        MetadataReader reader = new SimpleMetadataReader(resource);

        AnnotationMetadata amd = reader.getAnnotationMetadata();

        String annotation = Component.class.getName();

        Assert.assertTrue(amd.hasAnnotation(annotation));
        AnnotationAttributes attributes = amd.getAnnotationAttributes(annotation);
        Assert.assertEquals("petStore", attributes.get("value"));

        Assert.assertFalse(amd.isAbstract());
        Assert.assertFalse(amd.isFinal());
        Assert.assertEquals("org.whyspring.service.v4.PetStoreService", amd.getClassName());
    }
}
