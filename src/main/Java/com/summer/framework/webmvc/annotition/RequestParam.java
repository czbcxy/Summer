package com.summer.framework.webmvc.annotition;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {

    String DEFAULT_NONE = "\n\t\t\n\t\t\n\uE000\uE001\uE002\n\t\t\t\t\n";

    String name() default "";

    boolean required() default true;

    String defaultValue() default DEFAULT_NONE;
}