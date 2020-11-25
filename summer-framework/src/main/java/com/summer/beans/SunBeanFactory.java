package com.summer.beans;


import com.summer.beans.factory.config.SunRootBeanDefinition;

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

    default String toLowerCaseAtFirst(String beanClass) {
        char[] chars = beanClass.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
