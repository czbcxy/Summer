package com.summer.framework.context;

import com.summer.framework.beans.factory.config.SunBeanDefinition;

public interface SunConfigurableApplicationContext extends SunApplicationContext {

    /**
     * 最重要的refesh模板方法
     */
    SunBeanDefinition getBeanDefinition(String beanName);
}
