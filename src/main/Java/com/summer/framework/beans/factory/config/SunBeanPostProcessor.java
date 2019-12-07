package com.summer.framework.beans.factory.config;

/**
 * 前后置处理器
 */
public interface SunBeanPostProcessor {

    default Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    default Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}
