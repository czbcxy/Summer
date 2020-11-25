package com.summer.beans;

/**
 * BeanWrapper
 */
public interface SunBeanWrapper {

    /**
     * Return the bean instance wrapped by this object.
     */
    Object getWrappedInstance();

    /**
     * Return the type of the wrapped bean instance.
     */
    Class<?> getWrappedClass();

}
