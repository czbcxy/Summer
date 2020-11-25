package com.summer.context.support;

import java.util.Objects;

/**
 * map<>
 */
public class SunGenericApplicationContext extends SunAbstractApplicationContext {

    public static SunGenericApplicationContext context;

    public SunGenericApplicationContext() {
    }

    public static synchronized SunGenericApplicationContext getInstance() {
        return Objects.isNull(context) ? new SunGenericApplicationContext() : context;
    }
}
