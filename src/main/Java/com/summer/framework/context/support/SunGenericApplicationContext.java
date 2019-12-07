package com.summer.framework.context.support;

/**
 * @ list<beandefinition>
 * map<>
 */
public class SunGenericApplicationContext extends SunAbstractApplicationContext {

    public SunGenericApplicationContext(String... configLoactions) {
        super.configLoactions = configLoactions;
        if (configLoactions.length > 0) {
            super.refresh();
        }
    }

    public void setCondigLocations(String... configLoactions) {
        super.configLoactions = configLoactions;
        if (configLoactions.length > 0) {
            super.refresh();
        }
    }

}
