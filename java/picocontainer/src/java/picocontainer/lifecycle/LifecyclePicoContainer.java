/*****************************************************************************
 * Copyright (C) PicoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the license.html file.                                                    *
 *                                                                           *
 * Idea by Rachel Davies, Original code by Aslak Hellesoy and Paul Hammant   *
 *****************************************************************************/

package picocontainer.lifecycle;

import picocontainer.ComponentFactory;
import picocontainer.PicoContainer;
import picocontainer.PicoInstantiationException;
import picocontainer.defaults.NullContainer;
import picocontainer.defaults.DefaultComponentFactory;
import picocontainer.hierarchical.HierarchicalPicoContainer;

public class LifecyclePicoContainer extends HierarchicalPicoContainer implements Startable, Stoppable, Disposable {

    private Startable startableAggregatedComponent;
    private Stoppable stoppableAggregatedComponent;
    private Disposable disposableAggregatedComponent;
    private boolean started;
    private boolean disposed;

    public LifecyclePicoContainer(ComponentFactory componentFactory, PicoContainer parentContainer) {
        super(componentFactory, parentContainer);
    }

    public static class Default extends LifecyclePicoContainer {
        public Default() {
            super(new DefaultComponentFactory(), new NullContainer());
        }
    }

    public void instantiateComponents() throws PicoInstantiationException {
        super.instantiateComponents();
        try {
            startableAggregatedComponent = (Startable) getAggregateComponentProxy(true, false);
        } catch (ClassCastException e) {
        }
        try {

            stoppableAggregatedComponent = (Stoppable) getAggregateComponentProxy(false, false);
        } catch (ClassCastException e) {
        }
        try {

            disposableAggregatedComponent = (Disposable) getAggregateComponentProxy(false, false);
        } catch (ClassCastException e) {
        }

    }

    public void start() throws Exception {
        checkDisposed();
        if (started) {
            throw new IllegalStateException("Already started.");
        }
        started = true;
        if (startableAggregatedComponent != null) {
            startableAggregatedComponent.start();
        }
    }

    public void stop() throws Exception {
        checkDisposed();
        if (started == false) {
            throw new IllegalStateException("Already stopped.");
        }
        started = false;
        if (stoppableAggregatedComponent != null) {
            stoppableAggregatedComponent.stop();
        }
    }

    public void dispose() throws Exception {
        checkDisposed();
        disposed = true;
        if (disposableAggregatedComponent != null) {
            disposableAggregatedComponent.dispose();
        }
    }

    private void checkDisposed() {
        if (disposed) {
            throw new IllegalStateException("Components Disposed Of");
        }
    }

}
