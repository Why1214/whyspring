package org.whyspring.core.type.classreading;

import org.springframework.asm.AnnotationVisitor;
import org.springframework.asm.SpringAsmInfo;
import org.whyspring.core.annotation.AnnotationAttributes;

import java.util.Map;

public class AnnotationAttributesReadingVisitor extends AnnotationVisitor {

    private final String annotationType;

    private final Map<String, AnnotationAttributes> attributesMap;

    AnnotationAttributes attributes = new AnnotationAttributes();

    public AnnotationAttributesReadingVisitor(
            String annotationType, Map<String, AnnotationAttributes> attributesMap) {
        super(SpringAsmInfo.ASM_VERSION);

        this.annotationType = annotationType;
        this.attributesMap = attributesMap;
    }

    @Override
    public final void visitEnd() {
        // 属性解析完成之后，将注解名和注解的属性集合关联
        this.attributesMap.put(this.annotationType, this.attributes);
    }

    public void visit(String attributeName, Object attributeValue) {
        // 保存属性名和属性值
        this.attributes.put(attributeName, attributeValue);
    }
}
