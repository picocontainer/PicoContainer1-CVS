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
import org.nanocontainer.nanoaop.PointcutsFactory;

/**
 * @author Stephen Molitor
 */
public abstract class AbstractPointcutsFactory implements PointcutsFactory {

    public ComponentPointcut component(Object componentKey) {
        return new KeyEqualsComponentPointcut(componentKey);
    }

    public ComponentPointcut componentName(String regex) {
        return new NameMatchesComponentPointcut(regex);
    }

}