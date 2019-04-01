package org.whyspring.beans;

import java.util.List;

public interface BeanDefinition {

    public static final  String SCOPE_SINGLETON = "singleton";
    public static final String SCOPE_PROTOTYPE = "prototype";
    public static final String SCOPE_DEFAULT = "";

    public boolean isSingleton();
    public boolean isPrototype();

    public String getScope();
    public void setScope(String scope);

    String getBeanClassName();

    List<PropertyValue> getPropertyValues();

    ConstructorArgument getConstructorArgument();

    String getBeanId();

    boolean hasConstructorArgumentValues();
}
