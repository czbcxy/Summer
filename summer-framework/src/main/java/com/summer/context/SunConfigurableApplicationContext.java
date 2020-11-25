package com.summer.context;


import com.summer.beans.factory.config.SunBeanDefinition;

public interface SunConfigurableApplicationContext extends SunApplicationContext {

    /**
     * 最重要的refesh模板方法
     */
    SunBeanDefinition getBeanDefinition(String beanName);
}
