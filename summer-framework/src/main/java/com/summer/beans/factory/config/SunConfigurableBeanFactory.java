package com.summer.beans.factory.config;

import com.summer.beans.SunBeanFactory;

public interface SunConfigurableBeanFactory extends SunBeanFactory {

    String SCOPE_SINGLETON = "singleton";

    String SCOPE_PROTOTYPE = "prototype";

    void addBeanPostProcessor(SunBeanPostProcessor beanPostProcessor);
}
