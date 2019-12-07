package com.summer.framework.beans.factory.Support;

import com.summer.framework.beans.SunBeanFactory;
import com.summer.framework.beans.BeanUtils;
import com.summer.framework.beans.factory.config.SunRootBeanDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;

public class SunSimpleInstantiationStrategy implements SunInstantiationStrategy {

    @Override
    public Object instantiate(SunRootBeanDefinition bd, String beanName, SunBeanFactory owner) {
        // Don't override the class with CGLIB if no overrides.
//        if (!bd.hasMethodOverrides()) {
        Constructor<?> constructorToUse;
        synchronized (bd.constructorArgumentLock) {
            final Class<?> clazz;
            try {
                clazz = Class.forName(bd.getBeanClassName());
                if (System.getSecurityManager() != null) {
                    constructorToUse = AccessController.doPrivileged(
                            (PrivilegedExceptionAction<Constructor<?>>) clazz::getDeclaredConstructor);
                } else {
                    constructorToUse = clazz.getDeclaredConstructor();
                }
            } catch (Throwable ex) {
                throw new RuntimeException("No default constructor found", ex);
            }
        }
        return BeanUtils.instantiateClass(constructorToUse);
//        } else {
        // Must generate CGLIB subclass.
//            throw new UnsupportedOperationException("Method Injection not supported in SimpleInstantiationStrategy");
//        }
    }

    @Override
    public Object instantiate(SunRootBeanDefinition bd, String beanName, SunBeanFactory owner, Constructor<?> ctor, Object... args) {
        return null;
    }

    @Override
    public Object instantiate(SunRootBeanDefinition bd, String beanName, SunBeanFactory owner, Object factoryBean, Method factoryMethod, Object... args) {
        return null;
    }
}
