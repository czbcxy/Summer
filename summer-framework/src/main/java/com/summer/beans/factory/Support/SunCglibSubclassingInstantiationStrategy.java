package com.summer.beans.factory.Support;

import com.summer.beans.SunBeanFactory;
import com.summer.beans.factory.config.SunRootBeanDefinition;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class SunCglibSubclassingInstantiationStrategy extends SunSimpleInstantiationStrategy implements SunInstantiationStrategy {
    @Override
    public Object instantiate(SunRootBeanDefinition bd, String beanName, SunBeanFactory owner) {


        return null;
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
