/*****************************************************************************
 * Copyright (C) PicoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the license.html file.                                                    *
 *                                                                           *
 * Idea by Rachel Davies, Original code by Aslak Hellesoy and Paul Hammant   *
 *****************************************************************************/

package org.picocontainer.internals;

import org.picocontainer.internals.ComponentSpecification;
import org.picocontainer.PicoInitializationException;
import org.picocontainer.PicoIntrospectionException;

public interface ComponentFactory {

    /**
     * Create a component. Used by the internals of applicable PicoContainers
     * to instantiate a component.
     * @param componentSpec
     * @param instanceDependencies The component instances the created component will depend on.
     * @throws org.picocontainer.PicoInitializationException
     * @throws org.picocontainer.PicoIntrospectionException
     */
    Object createComponent(ComponentSpecification componentSpec, Object[] instanceDependencies) throws PicoInitializationException, PicoIntrospectionException;

    /**
     * Return the types the componentImplementation component depends on.
     * @param componentImplementation concrete component class.
     * @throws PicoIntrospectionException
     */
    Class[] getDependencies(Class componentImplementation) throws PicoIntrospectionException;
}
