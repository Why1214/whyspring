package org.whyspring.beans.factory.annotation;

import org.whyspring.beans.factory.config.AutowireCapableBeanFactory;

import java.lang.reflect.Member;

public abstract class InjectionElement {

    // 成员信息（此处主要是Field）
    protected Member member;

    // 该工厂提供处理依赖的能力
    protected AutowireCapableBeanFactory factory;

    InjectionElement(Member member, AutowireCapableBeanFactory factory) {
        this.member = member;
        this.factory = factory;
    }

    public abstract void inject(Object target);
}
