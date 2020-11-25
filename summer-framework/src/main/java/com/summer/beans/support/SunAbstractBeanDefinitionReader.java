package com.summer.beans.support;

import com.summer.annotation.Component;
import com.summer.annotation.Controller;
import com.summer.annotation.Service;
import com.summer.beans.SunBeanDefinitionReader;
import com.summer.beans.factory.config.SunBeanDefinition;
import com.summer.beans.factory.config.SunRootBeanDefinition;
import com.summer.webmvc.annotition.RestController;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认资源扫描器
 */
@Slf4j
public abstract class SunAbstractBeanDefinitionReader extends SunDefaultListableBeanFactory implements SunBeanDefinitionReader {

    protected static Class<?> bootApplication;
    protected static final Properties config = new Properties();
    protected List<String> registyBeanClass = new ArrayList<>();

    protected void doLoadApplicationProperties() {
        final URL propertieUrl = bootApplication.getResource("/");
        final File file = new File(propertieUrl.getPath());
        for (File listFile : Objects.requireNonNull(file.listFiles())) {
            final String lowerCase = listFile.getName().toLowerCase();
            if (lowerCase.endsWith(".properties") && lowerCase.startsWith("application")) {
                try (final FileInputStream io = new FileInputStream(listFile)) {
                    log.info("sunnmer load properties ==> {}", lowerCase);
                    config.load(io);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void doScanner() {
        String packages = bootApplication.getPackage().getName();
        URL resource = bootApplication.getClassLoader().getResource(packages.replace(".", "/"));
        assert resource != null;
        doScanner(resource.getFile(), packages);
    }

    protected void doScanner(String url, String packages) {
        File classPath = new File(url);
        for (File file : Objects.requireNonNull(classPath.listFiles())) {
            if (file.isDirectory()) {
                doScanner(url.concat("/").concat(file.getName()), packages);
            } else {
                if (!file.getName().endsWith(".class")) {
                    continue;
                }
                final String[] split = file.getPath().split(packages);
                if (split.length <= 0) {
                    continue;
                }
                registyBeanClass.add(packages + split[1].replace("/", ".").replace(".class", ""));
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
            log.info("size :==> {}", beanDefinitions.size());
            log.info("bean :==> {}", beanDefinitions);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected SunBeanDefinition doCreateBeanDefinition(String beanName) throws ClassNotFoundException {
        //反射回去类对象
        Class<?> clazz = Class.forName(beanName);
        //判断是否有注解，//接口-->实现类作为beanClassName
        if ((clazz.isAnnotationPresent(RestController.class) ||
                !clazz.isAnnotationPresent(Controller.class) ||
                !clazz.isAnnotationPresent(Service.class) ||
                !clazz.isAnnotationPresent(Component.class)) &&
                !clazz.isInterface()) {
            SunRootBeanDefinition definition = new SunRootBeanDefinition();
            definition.setBeanClassName(beanName);
            definition.setFactoryBeanName(toLowerCaseAtFirst(clazz.getSimpleName()));
            return definition;
        }
        return null;
    }

    //########################################################################
    //###########################beanner,小东西，就写在这玩玩###########################################
    //########################################################################
    public static void Banner() {
        String banner = " ▄▄▄▄▄▄▄▄▄▄▄  ▄         ▄  ▄▄       ▄▄  ▄▄       ▄▄  ▄▄▄▄▄▄▄▄▄▄▄  ▄▄▄▄▄▄▄▄▄▄▄      \n" +
                "▐░░░░░░░░░░░▌▐░▌       ▐░▌▐░░▌     ▐░░▌▐░░▌     ▐░░▌▐░░░░░░░░░░░▌▐░░░░░░░░░░░▌     \n" +
                "▐░█▀▀▀▀▀▀▀▀▀ ▐░▌       ▐░▌▐░▌░▌   ▐░▐░▌▐░▌░▌   ▐░▐░▌▐░█▀▀▀▀▀▀▀▀▀ ▐░█▀▀▀▀▀▀▀█░▌     \n" +
                "▐░▌          ▐░▌       ▐░▌▐░▌▐░▌ ▐░▌▐░▌▐░▌▐░▌ ▐░▌▐░▌▐░▌          ▐░▌       ▐░▌     \n" +
                "▐░█▄▄▄▄▄▄▄▄▄ ▐░▌       ▐░▌▐░▌ ▐░▐░▌ ▐░▌▐░▌ ▐░▐░▌ ▐░▌▐░█▄▄▄▄▄▄▄▄▄ ▐░█▄▄▄▄▄▄▄█░▌     \n" +
                "▐░░░░░░░░░░░▌▐░▌       ▐░▌▐░▌  ▐░▌  ▐░▌▐░▌  ▐░▌  ▐░▌▐░░░░░░░░░░░▌▐░░░░░░░░░░░▌     \n" +
                " ▀▀▀▀▀▀▀▀▀█░▌▐░▌       ▐░▌▐░▌   ▀   ▐░▌▐░▌   ▀   ▐░▌▐░█▀▀▀▀▀▀▀▀▀ ▐░█▀▀▀▀█░█▀▀      \n" +
                "          ▐░▌▐░▌       ▐░▌▐░▌       ▐░▌▐░▌       ▐░▌▐░▌          ▐░▌     ▐░▌       \n" +
                " ▄▄▄▄▄▄▄▄▄█░▌▐░█▄▄▄▄▄▄▄█░▌▐░▌       ▐░▌▐░▌       ▐░▌▐░█▄▄▄▄▄▄▄▄▄ ▐░▌      ▐░▌      \n" +
                "▐░░░░░░░░░░░▌▐░░░░░░░░░░░▌▐░▌       ▐░▌▐░▌       ▐░▌▐░░░░░░░░░░░▌▐░▌       ▐░▌     \n" +
                " ▀▀▀▀▀▀▀▀▀▀▀  ▀▀▀▀▀▀▀▀▀▀▀  ▀         ▀  ▀         ▀  ▀▀▀▀▀▀▀▀▀▀▀  ▀         ▀      \n" +
                "                                                                                   ";
        System.out.println(banner);
    }

}


