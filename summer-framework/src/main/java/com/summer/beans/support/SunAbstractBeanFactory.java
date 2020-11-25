package com.summer.beans.support;

import com.summer.beans.factory.config.SunBeanPostProcessor;
import com.summer.beans.factory.config.SunConfigurableBeanFactory;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class SunAbstractBeanFactory implements SunConfigurableBeanFactory {

    /**
     * BeanPostProcessors to apply in createBean.
     */
    private final List<SunBeanPostProcessor> beanPostProcessors = new CopyOnWriteArrayList<>();


    public void addBeanPostProcessor(SunBeanPostProcessor beanPostProcessor) {
        if (beanPostProcessor == null) {
            return;
        }
        // Remove from old position, if any
        this.beanPostProcessors.remove(beanPostProcessor);
        // Add to end of list
        this.beanPostProcessors.add(beanPostProcessor);
    }
}
