package com.tlj.aicodegenerator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AuthCheck {
    /**
     * 角色列表，必须包含这些角色才能访问该方法
     * @return
     */
    String mustRole() default "";
}

