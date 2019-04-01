package org.whyspring.core.type.classreading;

import org.springframework.asm.ClassVisitor;
import org.springframework.asm.Opcodes;
import org.springframework.asm.SpringAsmInfo;
import org.whyspring.util.ClassUtils;

public class ClassMetadataReadingVisitor extends ClassVisitor {

    // 类名
    private String className;

    // 是否是接口
    private boolean isInterface;

    // 是否是抽象的
    private boolean isAbstract;

    // 是否是不可变的
    private boolean isFinal;

    // 父类名
    private String superClassName;

    private String[] interfaces;

    public ClassMetadataReadingVisitor() {
        super(SpringAsmInfo.ASM_VERSION);
    }

    public void visit(int version, int access, String name, String signature, String supername, String[] interfaces) {
        this.className = ClassUtils.convertResourcePathToClassName(name);
        this.isInterface = ((access & Opcodes.ACC_INTERFACE) != 0);
        this.isAbstract = ((access & Opcodes.ACC_ABSTRACT) != 0);
        this.isFinal = ((access & Opcodes.ACC_FINAL) != 0);
        if (supername != null) {
            this.superClassName = ClassUtils.convertResourcePathToClassName(supername);
        }
        this.interfaces = new String[interfaces.length];
        for (int i = 0; i < interfaces.length; i++) {
            this.interfaces[i] = ClassUtils.convertResourcePathToClassName(interfaces[i]);
        }
    }

    public String getClassName() {
        return this.className;
    }

    public boolean isInterface() {
        return this.isInterface;
    }

    public boolean isAbstract() {
        return this.isAbstract;
    }

    public boolean isConcrete() {
        return !(this.isInterface || this.isAbstract);
    }

    public boolean isFinal() {
        return this.isFinal;
    }


    public boolean hasSuperClass() {
        return (this.superClassName != null);
    }

    public String getSuperClassName() {
        return this.superClassName;
    }

    public String[] getInterfaceNames() {
        return this.interfaces;
    }
}
