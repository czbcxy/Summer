package com.summer.framework.context;

import com.summer.framework.beans.factory.SunAware;

/**
 * @author czbcxy
 * @doc 容器感知类动作接口
 */
public interface SunApplicationContextAware extends SunAware {

    /**
     * 将有一个监听器扫描实现类，自动注入这个容器，达到动态改变ioc容器的目的。
     *
     * @param applicationContext
     */
    void setApplicationContext(SunApplicationContext applicationContext);

}
