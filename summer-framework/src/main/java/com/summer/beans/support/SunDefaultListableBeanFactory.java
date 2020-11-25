package com.summer.beans.support;


import com.summer.beans.SunBeanWrapper;
import com.summer.beans.factory.config.SunBeanDefinition;
import com.summer.beans.factory.config.SunRootBeanDefinition;
import com.summer.context.SunConfigurableApplicationContext;

/**
 * @author czbcxy
 * @doc 默认的实现类
 * extends SunAbstractAutowireCapableBeanFactory
 */
public abstract class SunDefaultListableBeanFactory extends SunAbstractAutowireCapableBeanFactory implements SunConfigurableApplicationContext {

    @Override
    public Object getBean(String beanName, SunRootBeanDefinition beanDefinition) {
        return doGetBean(beanName, beanDefinition);
    }

    @Override
    public Object getBean(String beanName) {
        return super.beanContextObjectsCache.get(beanName).getWrappedInstance();
    }

    /**
     * 依赖注入开始入口>反射
     * spring通过一个包装器beanwrapper来进行包装扩充，并不会动beandefinitionMap中的的原生类
     * 采用装饰器模式
     * 1，保留原来oop模式
     * 2，对其进行扩展，为后来的aop做准备。
     *
     * @param beanName
     * @return
     */
    private Object doGetBean(String beanName, SunRootBeanDefinition beanDefinition) {
        //init实例化
        SunBeanWrapper wrapper = doCreateBean(beanName, beanDefinition, null);
        beanContextObjectsCache.put(beanName, wrapper);

        //DI注入到beanwrapper
        populateBean(beanName, beanDefinition, wrapper);
        return beanContextObjectsCache.get(beanName).getWrappedInstance();
    }

    @Override
    public SunBeanDefinition getBeanDefinition(String beanName) {
        return this.beanDefinitionMap.get(beanName);
    }
}
