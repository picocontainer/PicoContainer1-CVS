/*****************************************************************************
 * Copyright (C) PicoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Idea by Rachel Davies, Original code by Aslak Hellesoy and Paul Hammant   *
 *****************************************************************************/

package org.picocontainer.composite;

import org.picocontainer.ComponentRegistry;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.DefaultComponentRegistry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Collection;
import java.util.Collections;
import java.io.Serializable;

/**
 * CompositePicoContainer2 aggregates the the contents of more
 * than one container together for the sake of a single list of
 * components. This list may be used as the parent container for
 * another PicoContainer. This will result in directive graphs of
 * containers/components rather than just trees.
 *
 * It is not in itself, a Pico component (the array in the
 * constructor puts paid to that).
 *
 */
public class CompositePicoContainer2 implements PicoContainer, Serializable {

    private final List containers = new ArrayList();
    private final ComponentRegistry componentRegistry;

    public CompositePicoContainer2(final ComponentRegistry componentRegistry,
                                  final PicoContainer[] containers) {
        this.componentRegistry = componentRegistry;

        if (containers == null) {
            throw new NullPointerException("containers can't be null");
        }
        for (int i = 0; i < containers.length; i++) {
            PicoContainer container = containers[i];
            if (container == null) {
                throw new NullPointerException("PicoContainer at position " + i + " was null");
            }
            this.containers.add(container);
        }
    }

    public static class Default extends CompositePicoContainer2 {
        public Default(final PicoContainer container) {
            super(new DefaultComponentRegistry(), new PicoContainer[]{container});
        }
    }

    public Object getComponent(Object componentKey) {
        Object answer = componentRegistry.getComponentInstance(componentKey);
        if (answer == null) {
            for (Iterator iter = containers.iterator(); iter.hasNext(); ) {
                PicoContainer container = (PicoContainer) iter.next();
                if (container.hasComponent(componentKey)) {
                    return container.getComponent(componentKey);
                }
            }
        }
        return answer;
    }

    public Collection getComponentKeys() {
        Set componentTypes = new HashSet();
        componentTypes.addAll(componentRegistry.getComponentInstanceKeys());
        for (Iterator iter = containers.iterator(); iter.hasNext(); ) {
            PicoContainer container = (PicoContainer) iter.next();
            componentTypes.addAll(container.getComponentKeys());
        }
        return Collections.unmodifiableCollection(componentTypes);
    }

    public void instantiateComponents() {
        throw new UnsupportedOperationException();
    }

    public boolean hasComponent(Object componentKey) {
        //TODO - review
        return componentRegistry.getComponentInstance(componentKey) != null;
    }

    public Collection getComponents() {
        Set componentTypes = new HashSet();
        componentTypes.addAll(componentRegistry.getComponentInstanceKeys());
        for (Iterator iter = containers.iterator(); iter.hasNext(); ) {
            PicoContainer container = (PicoContainer) iter.next();
            componentTypes.addAll(container.getComponentKeys());
        }
        List list = new ArrayList();
        for (Iterator iterator = componentTypes.iterator(); iterator.hasNext();) {
            Object key = (Object) iterator.next();
            list.add(getComponent(key));
        }
        return Collections.unmodifiableCollection(list);
    }

    public Object getCompositeComponent() {
        throw new UnsupportedOperationException();
    }

    public Object getCompositeComponent(boolean callInInstantiationOrder, boolean callUnmanagedComponents) {
        throw new UnsupportedOperationException();
    }

    /**
     * Adds a new Pico container to this composite container
     * @param container
     */
    protected void addContainer(PicoContainer container) {
        containers.add(container);
    }

    /**
     * Removes a Pico container from this composite container
     * @param container
     */
    protected void removeContainer(PicoContainer container) {
        containers.remove(container);
    }

    /**
     * A helper method which adds each of the objects in the array to the list
     *
     * @param list
     * @param objects
     */
    protected static void addAll(List list, Object[] objects) {
        for (int i = 0, size = objects.length; i < size; i++ ) {
            list.add(objects[i]);
        }
    }
}