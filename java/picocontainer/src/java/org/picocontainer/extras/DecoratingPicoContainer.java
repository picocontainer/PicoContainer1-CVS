/*****************************************************************************
 * Copyright (C) OldPicoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Idea by Rachel Davies, Original code by Aslak Hellesoy and Paul Hammant   *
 *****************************************************************************/

package org.picocontainer.extras;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.defaults.AmbiguousComponentResolutionException;
import org.picocontainer.defaults.AbstractPicoContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.Iterator;

/**
 * DecoratingPicoContainer aggregates the the contents of one or more
 * containers together. This list may be used to create graphs and/or
 * trees of containers.
 * <p>
 * It replaces the old HierarchicalComponentRegistry and
 * CompositePicoContainer.
 *
 * @author Paul Hammant
 * @author Aslak Helles&oslash;y
 * @version $Revision$
 */
public class DecoratingPicoContainer extends AbstractPicoContainer {

    private final List delegates = new ArrayList();

    public DecoratingPicoContainer() {
        super(null);
    }

    public Collection getComponentKeys() {
        List result = new ArrayList();
        for (Iterator iterator = delegates.iterator(); iterator.hasNext();) {
            AbstractPicoContainer delegate = (AbstractPicoContainer) iterator.next();
            result.addAll(delegate.getComponentKeys());
        }
        return result;
    }

    public ComponentAdapter findComponentAdapter(Object componentKey) throws AmbiguousComponentResolutionException {
        for (Iterator iterator = delegates.iterator(); iterator.hasNext();) {
            AbstractPicoContainer delegate = (AbstractPicoContainer) iterator.next();
            ComponentAdapter componentAdapter = delegate.findComponentAdapter(componentKey);
            if(componentAdapter != null) {
                return componentAdapter;
            }
        }
        return null;
    }

    public void unregisterComponent(Object componentKey) {
        throw new UnsupportedOperationException();
    }

    public void registerComponent(ComponentAdapter componentAdapter) {
        throw new UnsupportedOperationException();
    }

    public List getComponentAdapters() {
        throw new UnsupportedOperationException();
    }

    // END implementation of abstract superclass methods

    public void addDelegate(AbstractPicoContainer delegate) {
        delegates.add(delegate);
    }
}