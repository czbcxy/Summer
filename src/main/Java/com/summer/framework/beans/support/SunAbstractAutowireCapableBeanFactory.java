package com.summer.framework.beans.support;

import com.summer.framework.beans.factory.config.SunAutowireCapableBeanFactory;
import com.summer.framework.beans.factory.config.SunBeanDefinition;
import com.summer.framework.annotation.Autowired;
import com.summer.framework.annotation.Component;
import com.summer.framework.annotation.Controller;
import com.summer.framework.annotation.Service;
import com.summer.framework.beans.SunBeanFactory;
import com.summer.framework.beans.SunBeanWrapper;
import com.summer.framework.beans.SunBeanWrapperImpl;
import com.summer.framework.beans.factory.Support.SunCglibSubclassingInstantiationStrategy;
import com.summer.framework.beans.factory.Support.SunInstantiationStrategy;
import com.summer.framework.beans.factory.config.SunRootBeanDefinition;

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
        SunBeanWrapper wrapper = new SunBeanWrapperImpl(beanInstance);
        return wrapper;
    }

    //实例化bean
    @Override
    public Object instantiateBean(final String beanName, final SunRootBeanDefinition mbd) {
        String className = mbd.getBeanClassName();
        String factoryBeanName = mbd.getFactoryBeanName();
        Object beanInstance = null;
        final SunBeanFactory parent = this;
        try {
            //默认单利，不是单利不处理
            if (mbd.isSingleton()) {
                if (this.singletonObjects.containsKey(className) || this.singletonObjects.containsKey(factoryBeanName)) {
                    this.singletonObjects.get(className);
                } else {
                    beanInstance = Class.forName(className).newInstance();
//                    beanInstance = getInstantiationStrategy().instantiate(mbd, className, parent);
                    if (beanInstance == null) {
                        throw new RuntimeException("Beaninstance init exception!");
                    }
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
        //判断是否有注解，
        if (!clazz.isAnnotationPresent(Controller.class) && !clazz.isAnnotationPresent(Service.class) && !clazz.isAnnotationPresent(Component.class)) {
            return;
        }
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
            Object autowiredbeanWrapper = this.beanContextObjectsCache.get(toLowerCaseAtFirst(autowiredBeanName)).getWrappedInstance();
            field.set(wrappedInstance, autowiredbeanWrapper);
        } catch (Exception e) {
            throw new RuntimeException(e);
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

    protected String toLowerCaseAtFirst(String beanClass) {
        char[] chars = beanClass.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

}
