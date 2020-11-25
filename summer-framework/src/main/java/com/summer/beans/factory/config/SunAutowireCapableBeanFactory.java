package com.summer.beans.factory.config;

import com.summer.beans.SunBeanFactory;
import com.summer.beans.SunBeanWrapper;
import java.lang.reflect.Field;

/**
 * 自动注入DI定层接口
 */
public interface SunAutowireCapableBeanFactory extends SunBeanFactory {

    SunBeanWrapper doCreateBean(final String beanName, final SunRootBeanDefinition mbd, final Object[] args);

    Object instantiateBean(final String beanName, final SunRootBeanDefinition mbd) ;

    void populateBean(String beanName, SunBeanDefinition mbd, SunBeanWrapper bw);

    void autowireBean(Field field, Object wrappedInstance);

    Object initializeBean(Object existingBean, String beanName);

    Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName);

    Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName);

    void destroyBean(Object existingBean);
}

