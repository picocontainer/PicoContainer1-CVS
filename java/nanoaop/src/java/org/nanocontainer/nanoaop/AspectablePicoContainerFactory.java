/*****************************************************************************
 * Copyright (c) PicoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Idea by Rachel Davies, Original code by various                           *
 *****************************************************************************/
package org.nanocontainer.nanoaop;

import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.ComponentAdapterFactory;

/**
 * @author Stephen Molitor
 */
public interface AspectablePicoContainerFactory {

    AspectablePicoContainer createContainer(Class containerClass, ComponentAdapterFactory componentAdapterFactory,
            PicoContainer parent);
    
    AspectablePicoContainer createContainer(ComponentAdapterFactory componentAdapterFactory, PicoContainer parent);
    
    AspectablePicoContainer createContainer(ComponentAdapterFactory componentAdapterFactory);
    
    AspectablePicoContainer createContainer(PicoContainer parent);

    AspectablePicoContainer createContainer();

}