package com.summer.framework.beans;

import com.summer.framework.beans.factory.config.SunRootBeanDefinition;

/**
 * @author czbcxy
 * @doc BeanFactory
 */
public interface SunBeanFactory {

    /**
     * 获取实例Bean从IOC容器
     *
     * @param beanName
     * @param sunRootBeanDefinition
     * @return
     */
    Object getBean(String beanName, SunRootBeanDefinition sunRootBeanDefinition);

    Object getBean(String beanName);

}
