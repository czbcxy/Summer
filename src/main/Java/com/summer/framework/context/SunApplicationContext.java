package com.summer.framework.context;

import com.summer.framework.beans.SunBeanFactory;
import com.summer.framework.beans.factory.config.SunRootBeanDefinition;

import java.util.Properties;

/**
 * @author czbcxy
 * @doc beanFactory的重要实现类
 */
public interface SunApplicationContext extends SunBeanFactory {

    @Override
    Object getBean(String beanName, SunRootBeanDefinition sunRootBeanDefinition);

    String[] getBeanDefinitionNames();

    int getBeanDifinitionCount();

    Properties getConfig();
}
