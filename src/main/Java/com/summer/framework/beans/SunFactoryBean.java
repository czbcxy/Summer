package com.summer.framework.beans;


/**
 * @author czbxy
 * @doc FactoryBean
 */
public interface SunFactoryBean<T> {

    String OBJECT_TYPE_ATTRIBUTE = "factoryBeanObjectType";

//    @Nullable
    T getObject() throws Exception;

//    @Nullable
    Class<?> getObjectType();

    default boolean isSingleton(){return false;}
}
