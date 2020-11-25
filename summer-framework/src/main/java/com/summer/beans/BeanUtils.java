/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.summer.beans;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

/**
 * Static convenience methods for JavaBeans: for instantiating beans,
 * checking bean property types, copying bean properties, etc.
 *
 * <p>Mainly for use within the framework, but to some degree also
 * useful for application classes.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @author Sam Brannen
 * @author Sebastien Deleuze
 */
public abstract class BeanUtils {

    /**
     * Convenience method to instantiate a class using the given constructor.
     * <p>Note that this method tries to set the constructor accessible if given a
     * non-accessible (that is, non-public) constructor, and supports Kotlin classes
     * with optional parameters and default values.
     *
     * @param ctor the constructor to instantiate
     * @param args the constructor arguments to apply (use {@code null} for an unspecified
     *             parameter, Kotlin optional parameters and Java primitive types are supported)
     * @return the new instance
     * @see Constructor#newInstance
     * <p>
     * 实例化类根据指定的构造函数和参数
     */
    public static <T> T instantiateClass(Constructor<T> ctor, Object... args) {
        if (ctor == null) {
            throw new RuntimeException("Constructor must not be null");
        }
        try {
            if ((!Modifier.isPublic(ctor.getModifiers()) ||
                    !Modifier.isPublic(ctor.getDeclaringClass().getModifiers())) && !ctor.isAccessible()) {
                ctor.setAccessible(true);
            }
            Class<?>[] parameterTypes = ctor.getParameterTypes();
//                Assert.isTrue(args.length <= parameterTypes.length, "Can't specify more arguments than constructor parameters");
            Object[] argsWithDefaultValues = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                argsWithDefaultValues[i] = args[i];
            }
            return ctor.newInstance(argsWithDefaultValues);
        } catch (InstantiationException ex) {
            throw new RuntimeException("Is it an abstract class?", ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException("Is the constructor accessible?", ex);
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("Illegal arguments for constructor", ex);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException("Constructor threw exception", ex.getTargetException());
        }
    }
}
