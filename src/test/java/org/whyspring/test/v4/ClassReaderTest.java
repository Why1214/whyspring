package org.whyspring.test.v4;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.asm.ClassReader;
import org.whyspring.core.annotation.AnnotationAttributes;
import org.whyspring.core.io.ClassPathResource;
import org.whyspring.core.type.classreading.AnnotationMetadataReadingVisitor;
import org.whyspring.core.type.classreading.ClassMetadataReadingVisitor;

import java.io.IOException;

public class ClassReaderTest {

    @Test
    public void testGetClassMetaData() throws IOException {
        ClassPathResource resource = new ClassPathResource("org/whyspring/service/v4/PetStoreService.class");
        ClassReader classReader = new ClassReader(resource.getInputStream());

        ClassMetadataReadingVisitor visitor = new ClassMetadataReadingVisitor();

        classReader.accept(visitor, ClassReader.SKIP_DEBUG);

        Assert.assertFalse(visitor.isAbstract());
        Assert.assertFalse(visitor.isInterface());
        Assert.assertFalse(visitor.isFinal());
        Assert.assertEquals("org.whyspring.service.v4.PetStoreService", visitor.getClassName());
        Assert.assertEquals("java.lang.Object", visitor.getSuperClassName());
        Assert.assertEquals(0, visitor.getInterfaceNames().length);
    }

    @Test
    public void testGetAnnotation() throws IOException {
        ClassPathResource resource = new ClassPathResource("org/whyspring/service/v4/PetStoreService.class");
        ClassReader classReader = new ClassReader(resource.getInputStream());

        AnnotationMetadataReadingVisitor visitor = new AnnotationMetadataReadingVisitor();

        classReader.accept(visitor, ClassReader.SKIP_DEBUG);

        String annotation = "org.whyspring.stereotype.Component";
        Assert.assertTrue(visitor.hasAnnotation(annotation));

        AnnotationAttributes attributes = visitor.getAnnotationAttributes(annotation);

        Assert.assertEquals("petStore", attributes.get("value"));
    }
}
