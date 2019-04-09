package org.whyspring.aop;

public interface Pointcut {

    // 获取一个方法匹配器
    MethodMatcher getMethodMatcher();

    // 获取表达式
    String getExpression();
}
