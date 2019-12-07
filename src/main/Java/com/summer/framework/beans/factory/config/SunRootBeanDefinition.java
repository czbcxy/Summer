package com.summer.framework.beans.factory.config;

import lombok.Data;

@Data
public class SunRootBeanDefinition extends SunAbstractBeanDefinition {
    public final Object constructorArgumentLock = new Object();

    public String beanClassName;
    public boolean lazyInit = false;
    public String factoryBeanName;
    public boolean isSingleton = true;

}
