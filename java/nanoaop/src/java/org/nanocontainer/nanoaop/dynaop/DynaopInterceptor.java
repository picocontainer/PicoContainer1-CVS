/*****************************************************************************
 * Copyright (c) PicoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Idea by Rachel Davies, Original code by various                           *
 *****************************************************************************/
package org.nanocontainer.nanoaop.dynaop;

import org.nanocontainer.nanoaop.Interceptor;

/**
 * @author Stephen Molitor
 */
public class DynaopInterceptor implements dynaop.Interceptor {

    private final Interceptor delegate;

    public DynaopInterceptor(Interceptor delegate) {
        this.delegate = delegate;
    }

    public Object intercept(dynaop.Invocation invocation) throws Throwable {
        return delegate.intercept(new DynaopInvocation(invocation));
    }

}