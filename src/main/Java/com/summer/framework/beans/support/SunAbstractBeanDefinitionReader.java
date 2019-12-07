package com.summer.framework.beans.support;

import com.summer.framework.beans.SunBeanDefinitionReader;
import com.summer.framework.beans.factory.config.SunBeanDefinition;
import com.summer.framework.beans.factory.config.SunRootBeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 默认资源扫描器
 */
public abstract class SunAbstractBeanDefinitionReader extends SunDefaultListableBeanFactory implements SunBeanDefinitionReader {

    protected static final Properties config = new Properties();
    protected List<String> registyBeanClass = new ArrayList<>();

    protected void scanLoadBeanDefinitions(String[] configLoactions) {
        InputStream io = null;
        try {
            io = this.getClass().getClassLoader()
                    .getResourceAsStream(configLoactions[0].replace("classpath:", ""));
            config.load(io);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (io == null) {
                try {
                    io.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void doScanner(String basePackage) {
        URL url = this.getClass().getClassLoader().getResource(basePackage);
        File classPath = new File(url.getFile());
        for (File file : classPath.listFiles()) {
            if (file.isDirectory()) {
                doScanner(basePackage + "/" + file.getName());
            } else {
                if (!file.getName().endsWith(".class")) {
                    continue;
                }
                String className = (basePackage.replace("/", ".") + "." + file.getName()).replace(".class", "");
                registyBeanClass.add(className);
            }
        }
    }

    public Properties getConfig() {
        return config;
    }

    @Override
    public void loadBeanDefinitions() {
        try {
            for (String beanName : registyBeanClass) {
                SunBeanDefinition sunBeanDefinition = doCreateBeanDefinition(beanName);
                if (sunBeanDefinition != null) {
                    super.beanDefinitions.add(sunBeanDefinition);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected SunBeanDefinition doCreateBeanDefinition(String beanName) throws ClassNotFoundException {
        //反射回去类对象
        Class<?> clazz = Class.forName(beanName);
        //接口-->实现类作为beanClassName
        if (!clazz.isInterface()) {
            SunRootBeanDefinition definition = new SunRootBeanDefinition();
            definition.setBeanClassName(beanName);
            definition.setFactoryBeanName(toLowerCaseAtFirst(clazz.getSimpleName()));
            return definition;
        }
        return null;
    }

    protected String toLowerCaseAtFirst(String beanClass) {
        char[] chars = beanClass.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

}


