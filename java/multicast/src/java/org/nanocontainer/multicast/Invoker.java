package org.picoextras.multicast;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author Aslak Helles&oslash;y
 * @version $Revision$
 */
public interface Invoker {
    void invoke(Object[] targets, Class declaringClass, Method method, Object[] args, List results, InvocationInterceptor invocationInterceptor) throws IllegalAccessException, InvocationTargetException;
}