package com.summer.framework.beans.factory.Support;

import com.summer.framework.beans.SunBeanFactory;
import com.summer.framework.beans.factory.config.SunRootBeanDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Interface responsible for creating instances corresponding to a root bean definition.
 *
 * <p>This is pulled out into a strategy as various approaches are possible,
 * including using CGLIB to create subclasses on the fly to support Method Injection.
 * <p>
 * 策略模式，cglib和jdk动态代理
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @since 1.1
 */
public interface SunInstantiationStrategy {


    Object instantiate(SunRootBeanDefinition bd, String beanName, SunBeanFactory owner);

    Object instantiate(SunRootBeanDefinition bd, String beanName, SunBeanFactory owner,
                       Constructor<?> ctor, Object... args);

    Object instantiate(SunRootBeanDefinition bd, String beanName, SunBeanFactory owner,
                       Object factoryBean, Method factoryMethod, Object... args);
}
