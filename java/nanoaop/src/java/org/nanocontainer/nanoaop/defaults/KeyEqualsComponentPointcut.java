/*****************************************************************************
 * Copyright (c) PicoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Idea by Rachel Davies, Original code by various                           *
 *****************************************************************************/
package org.nanocontainer.nanoaop.defaults;

import org.nanocontainer.nanoaop.ComponentPointcut;

/**
 * @author Stephen Molitor
 */
public class KeyEqualsComponentPointcut implements ComponentPointcut {

    private final Object componentKey;

    public KeyEqualsComponentPointcut(Object componentKey) {
        if (componentKey == null) {
            throw new NullPointerException("componentKey cannot be null");
        }
        this.componentKey = componentKey;
    }

    public boolean picks(Object componentKey) {
        return this.componentKey.equals(componentKey);
    }

}