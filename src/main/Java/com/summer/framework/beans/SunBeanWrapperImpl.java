package com.summer.framework.beans;

/**
 * beanWrapperImpl
 */
public class SunBeanWrapperImpl implements SunBeanWrapper {

    private Class<?> wrappedClass;
    private Object wrappedInstance;

    public SunBeanWrapperImpl(){}

    public SunBeanWrapperImpl(Object beanInstance) {
        this.wrappedInstance = beanInstance;
    }


    /**
     * 单利直接获取
     *
     * @return
     */
    @Override
    public Object getWrappedInstance() {
        if (this.wrappedInstance == null) {
            new RuntimeException("No wrapped object");
        }
        return this.wrappedInstance;
    }

    /**
     * 不是单利，每次获取。
     *
     * @return
     */
    @Override
    public Class<?> getWrappedClass() {
        return getWrappedInstance().getClass();
    }
}
