package org.whyspring.core.type;

public interface ClassMetadata {
    /**
     * 类名
     */
    String getClassName();

    /**
     * 是否为接口
     */
    boolean isInterface();

    /**
     * 是否抽象
     */
    boolean isAbstract();

    /**
     * 是否不可变
     */
    boolean isFinal();

    /**
     * 是否有父类
     */
    boolean hasSuperClass();

    /**
     * 父类名
     */
    String getSuperClassName();

    /**
     * 接口集合
     */
    String[] getInterfaceNames();
}
