package com.summer.beans.support;

import com.summer.annotation.Autowired;
import com.summer.annotation.Component;
import com.summer.annotation.Controller;
import com.summer.annotation.Service;
import com.summer.beans.SunBeanWrapper;
import com.summer.beans.SunBeanWrapperImpl;
import com.summer.beans.factory.Support.SunCglibSubclassingInstantiationStrategy;
import com.summer.beans.factory.Support.SunInstantiationStrategy;
import com.summer.beans.factory.config.SunAutowireCapableBeanFactory;
import com.summer.beans.factory.config.SunBeanDefinition;
import com.summer.beans.factory.config.SunRootBeanDefinition;
import com.summer.webmvc.annotition.RestController;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ioc实例化流程
 */
public abstract class SunAbstractAutowireCapableBeanFactory implements SunAutowireCapableBeanFactory {
    protected final List<SunBeanDefinition> beanDefinitions = new ArrayList<>(16);

    /**
     * Map of bean definition objects, keyed by bean name.
     */
    protected final Map<String, SunBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

    /**
     * singleton Ioc context
     *
     * @param beanName
     * @return
     */
    protected final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(64);

    /**
     * 通用的ioc容器
     */
    protected final Map<String, SunBeanWrapper> beanContextObjectsCache = new ConcurrentHashMap<>(64);


    private SunInstantiationStrategy instantiationStrategy = new SunCglibSubclassingInstantiationStrategy();

    public SunBeanWrapper doCreateBean(final String beanName, final SunRootBeanDefinition mbd, final Object[] args) {
        //todo
        //做process，类初始化前后初始化一些东西
        Object beanInstance = this.instantiateBean(beanName, mbd);
        //做process
        return new SunBeanWrapperImpl(beanInstance);
    }

    //实例化bean
    @Override
    public Object instantiateBean(final String beanName, final SunRootBeanDefinition mbd) {
        String className = mbd.getBeanClassName();
        String factoryBeanName = mbd.getFactoryBeanName();
        Object beanInstance = null;
        try {
            //默认单利，不是单利不处理
            if (mbd.isSingleton()) {
                if (this.singletonObjects.containsKey(className) || this.singletonObjects.containsKey(factoryBeanName)) {
                    beanInstance = this.singletonObjects.get(factoryBeanName);
                } else {
                    beanInstance = Class.forName(className).newInstance();
                    this.singletonObjects.put(className, beanInstance);
                    this.singletonObjects.put(factoryBeanName, beanInstance);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return beanInstance;
    }

    //填充bean
    public void populateBean(String beanName, SunBeanDefinition mbd, SunBeanWrapper bw) {
        Object wrappedInstance = bw.getWrappedInstance();
        Class<?> clazz = bw.getWrappedClass();
        //获取所有字段
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Autowired.class)) {
                continue;
            }
            autowireBean(field, wrappedInstance);
        }
    }

    @Override
    public void autowireBean(Field field, Object wrappedInstance) {
        Autowired autowired = field.getAnnotation(Autowired.class);
        String autowiredBeanName = autowired.value().trim();
        if ("".equals(autowiredBeanName)) {
            autowiredBeanName = field.getType().getSimpleName();
        }
        field.setAccessible(true);
        try {
            Object autowiredbeanWrapper = this.singletonObjects.get(toLowerCaseAtFirst(autowiredBeanName));
            field.set(wrappedInstance, autowiredbeanWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object initializeBean(Object existingBean, String beanName) {
//            wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
//            wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
        return null;
    }

    //aop前置，
    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) {
        return null;
    }

    //aop后置处理器
    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) {
        return null;
    }

    @Override
    public void destroyBean(Object existingBean) {
    }


    /**
     * Return the instantiation strategy to use for creating bean instances.
     */
    protected SunInstantiationStrategy getInstantiationStrategy() {
        return this.instantiationStrategy;
    }

//    public abstract void refresh();
}
