package com.summer.framework.context.support;

import com.summer.framework.beans.factory.config.SunBeanDefinition;
import com.summer.framework.beans.factory.config.SunRootBeanDefinition;
import com.summer.framework.beans.support.SunAbstractBeanDefinitionReader;
import com.summer.framework.context.SunConfigurableApplicationContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author czbcxy
 * @doc 抽象的容器
 */
@Slf4j
public abstract class SunAbstractApplicationContext extends SunAbstractBeanDefinitionReader implements SunConfigurableApplicationContext {

    private static final String SCAN_PACKAGE = "basePackage";
    private final Object startupShutdownMonitor = new Object();
    protected String[] configLoactions;

    public void refresh() {
        synchronized (this.startupShutdownMonitor) {
            //加载banner
            Banner();
            //1，定位配置文件
            scanLoadBeanDefinitions(configLoactions);
            log.info("BaseConfig info => {} ", config.toString());
            //2，加载配置文件，扫描相关的类，
            doScanner(config.getProperty(SCAN_PACKAGE));
            log.info("ScannerBeanClass info => {} ", registyBeanClass);
            //3,把他们封装成BeanDefinition
            loadBeanDefinitions();
            log.info("loadBeanDefinitions info => {} ", beanDefinitions);
            //4，注册，吧配置信息放到容器里面（伪ioc容器）（真ioc伪beanWarap）
            doRegisterBeanDefinition(beanDefinitions);
            log.info("beanDefinitionMap info => {} ", Arrays.toString(beanDefinitionMap.entrySet().toArray()));
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
            try {
                //全部实力化解决循环依赖问题。
                String className = sunRootBeanDefinition.beanClassName;
                Object instance = Class.forName(className).newInstance();
                singletonObjects.put(sunRootBeanDefinition.getFactoryBeanName(), instance);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
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
