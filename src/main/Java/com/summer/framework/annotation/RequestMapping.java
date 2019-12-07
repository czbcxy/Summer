package com.summer.framework.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
    String[] value() default {};
    RequestMethod[] method() default {};
//    String[] params() default {};
//    String[] headers() default {};
//    String[] consumes() default {};
//    String[] produces() default {};
}
