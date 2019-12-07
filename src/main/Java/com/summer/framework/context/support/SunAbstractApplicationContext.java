package com.summer.framework.context.support;

import com.summer.framework.beans.factory.config.SunBeanDefinition;
import com.summer.framework.beans.factory.config.SunRootBeanDefinition;
import com.summer.framework.beans.support.SunAbstractBeanDefinitionReader;
import com.summer.framework.context.SunConfigurableApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author czbcxy
 * @doc 抽象的容器
 */
public abstract class SunAbstractApplicationContext extends SunAbstractBeanDefinitionReader implements SunConfigurableApplicationContext {

    private static final String SCAN_PACKAGE = "basePackage";
    private final Object startupShutdownMonitor = new Object();
    protected String[] configLoactions;

    public void refresh() {
        synchronized (this.startupShutdownMonitor) {
            //1，定位配置文件
            scanLoadBeanDefinitions(configLoactions);
            //2，加载配置文件，扫描相关的类，
            doScanner(config.getProperty(SCAN_PACKAGE));
            //3,把他们封装成BeanDefinition
            loadBeanDefinitions();
            //4，注册，吧配置信息放到容器里面（伪ioc容器）（真ioc伪beanWarap）
            doRegisterBeanDefinition(beanDefinitions);
            //5，初始化类（不包含延迟加载的类）
            finishBeanFactoryInitialization();
        }
    }

    //只处理不是lazy的方法
    private void finishBeanFactoryInitialization() {
        for (Map.Entry<String, SunBeanDefinition> beanDefinitionEntry : beanDefinitionMap.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();
            SunRootBeanDefinition sunRootBeanDefinition = (SunRootBeanDefinition) beanDefinitionEntry.getValue();
            //不是延迟加载处理
            if (!sunRootBeanDefinition.isLazyInit()) {
                getBean(beanName, sunRootBeanDefinition);
            }
        }
    }

    private void doRegisterBeanDefinition(List<SunBeanDefinition> beanDefinitions) {
        for (SunBeanDefinition definition : beanDefinitions) {
            SunRootBeanDefinition sunRootBeanDefinition = (SunRootBeanDefinition) definition;
            beanDefinitionMap.put(sunRootBeanDefinition.getFactoryBeanName(), sunRootBeanDefinition);
        }
    }

    @Override
    public Object getBean(String beanName) {
        return super.beanContextObjectsCache.get(beanName).getWrappedInstance();
    }

    //获取所有实例对象名
    public String[] getBeanDefinitionNames() {
        return this.beanContextObjectsCache.keySet().toArray(new String[this.beanContextObjectsCache.size()]);
    }

    //instance count
    public int getBeanDifinitionCount() {
        return this.beanContextObjectsCache.size();
    }

    //获取配置文件
    public Properties getConfig() {
        return config;
    }

}
