package org.whyspring.core.type.classreading;

import org.springframework.asm.AnnotationVisitor;
import org.springframework.asm.Type;
import org.whyspring.core.annotation.AnnotationAttributes;
import org.whyspring.core.type.AnnotationMetadata;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class AnnotationMetadataReadingVisitor extends ClassMetadataReadingVisitor implements AnnotationMetadata {

    private final Set<String> annotationSet = new LinkedHashSet<String>(4);

    private final Map<String, AnnotationAttributes> attributeMap = new LinkedHashMap<String, AnnotationAttributes>(4);

    public AnnotationMetadataReadingVisitor() {
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String desc, boolean visible) {
        // 获取注解的类路径名
        String className = Type.getType(desc).getClassName();
        // 将名称保存到annotationSet集合中
        this.annotationSet.add(className);
        // 解析该注解的属性，并属性整合到AnnotationAttributes对象中，将类名和属性集合注册到attributeMap中
        return new AnnotationAttributesReadingVisitor(className, this.attributeMap);
    }

    public Set<String> getAnnotationTypes() {
        return this.annotationSet;
    }

    public boolean hasAnnotation(String annotationType) {
        return this.annotationSet.contains(annotationType);
    }

    public AnnotationAttributes getAnnotationAttributes(String annotationType) {
        return this.attributeMap.get(annotationType);
    }
}
