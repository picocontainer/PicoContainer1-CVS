/*****************************************************************************
 * Copyright (C) PicoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Idea by Rachel Davies, Original code by Aslak Hellesoy and Paul Hammant   *
 *****************************************************************************/

package picocontainer;

public interface ClassRegistrationPicoContainer extends LifecycleContainer {

    /**
     * Registers a component. Same as calling {@link #registerComponent(java.lang.Class, java.lang.Class)}
     * with the same argument.
     *
     * @param componentImplementation The class of the component to instantiate
     * @throws PicoRegistrationException If a registration problem
     */
    void registerComponent(Class componentImplementation)
            throws PicoRegistrationException;

    /**
     * Alternate way of registering components with additional
     * component type.
     *
     * @param componentType Component type
     * @param componentImplementation The class of the component to instantiate
     * @throws PicoRegistrationException If a registration problem
     */
    void registerComponent(Class componentType, Class componentImplementation)
            throws PicoRegistrationException;

    /**
     * Registers a component that is instantiated and configured outside
     * the container. Useful in cases where pico doesn't have sufficient
     * knowledge to instantiate a component.
     *
     * @param componentType Component type
     * @param component preinstantiated component
     * @throws PicoRegistrationException If a registration problem
     */
    void registerComponent(Class componentType, Object component)
            throws PicoRegistrationException;

    /**
     * Registers an instantiated component.  This might be because you are
     * creating trees of Pico containers or if you have a class that you want treated
     * as a component, but is not Pico component compatible.
     * @param component The pre instantiated component to register
     * @throws PicoRegistrationException
     */
    void registerComponent(Object component)
            throws PicoRegistrationException;

    /**
     * Add a parameter to a component. Used for configuring them.
     * Very liekly to change before release.
     * @param componentType The component type
     * @param parameter The parameter it pertains to
     * @param arg The argukemt to pass in.
     */
    void addParameterToComponent(Class componentType, Class parameter, Object arg);

}