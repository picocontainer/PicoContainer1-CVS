/*****************************************************************************
 * Copyright (c) PicoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Idea by Rachel Davies, Original code by Aslak Hellesoy and Paul Hammant   *
 *****************************************************************************/

package org.picocontainer.alternatives;

import junit.framework.TestCase;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoVerificationException;
import org.picocontainer.defaults.DefaultPicoContainer;
import org.picocontainer.alternatives.ImmutableComponentAdapter;
import org.picocontainer.alternatives.ImmutablePicoContainer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Paul Hammant
 * @version $Revision$
 */
public class ImmutablePicoContainerTestCase extends TestCase {

    public void testDelegationOfGettingComponentInstance() {
        DefaultPicoContainer mpc = new DefaultPicoContainer();
        mpc.registerComponentImplementation(Map.class, HashMap.class);
        ImmutablePicoContainer ipc = new ImmutablePicoContainer(mpc);
        Map map = (Map) ipc.getComponentInstance(Map.class);
        assertNotNull(map);
    }

    public void testDelegationOfGettingComponentInstanceOfType() {
        DefaultPicoContainer mpc = new DefaultPicoContainer();
        mpc.registerComponentImplementation(Map.class, HashMap.class);
        ImmutablePicoContainer ipc = new ImmutablePicoContainer(mpc);
        Map map = (Map) ipc.getComponentInstanceOfType(Map.class);
        assertNotNull(map);
    }

    public void testDelegationOfGetComponentInstances() {
        DefaultPicoContainer mpc = new DefaultPicoContainer();
        mpc.registerComponentImplementation(Map.class, HashMap.class);
        ImmutablePicoContainer ipc = new ImmutablePicoContainer(mpc);
        List comps = ipc.getComponentInstances();
        assertNotNull(comps);
        assertEquals(1, comps.size());
    }

    public void testDelegationOfGetComponentAdapter() {
        DefaultPicoContainer mpc = new DefaultPicoContainer();
        mpc.registerComponentImplementation(Map.class, HashMap.class);
        ImmutablePicoContainer ipc = new ImmutablePicoContainer(mpc);
        ComponentAdapter ca = ipc.getComponentAdapter(Map.class);
        assertNotNull(ca);
        assertTrue(ca instanceof ImmutableComponentAdapter);
        PicoContainer container = ca.getContainer();
        PicoContainer container2 = ca.getContainer();
        assertTrue(container instanceof ImmutablePicoContainer);
        assertTrue(container2 instanceof ImmutablePicoContainer);
        assertSame(container, container2);

    }

    public void testDelegationOfGetComponentAdapterOfType() {
        DefaultPicoContainer mpc = new DefaultPicoContainer();
        mpc.registerComponentImplementation(Map.class, HashMap.class);
        ImmutablePicoContainer ipc = new ImmutablePicoContainer(mpc);
        ComponentAdapter ca = ipc.getComponentAdapterOfType(Map.class);
        assertNotNull(ca);
        assertTrue(ca instanceof ImmutableComponentAdapter);
    }

    public void testDelegationOfGetComponentAdapters() {
        DefaultPicoContainer mpc = new DefaultPicoContainer();
        mpc.registerComponentImplementation(Map.class, HashMap.class);
        ImmutablePicoContainer ipc = new ImmutablePicoContainer(mpc);
        Collection comps = ipc.getComponentAdapters();
        assertNotNull(comps);
        assertEquals(1, comps.size());
    }

    public void testDelegationOfGetComponentAdaptersOfType() {
        DefaultPicoContainer mpc = new DefaultPicoContainer();
        mpc.registerComponentImplementation(Map.class, HashMap.class);
        ImmutablePicoContainer ipc = new ImmutablePicoContainer(mpc);
        List comps = ipc.getComponentAdaptersOfType(Map.class);
        assertNotNull(comps);
        assertEquals(1, comps.size());
    }

    public static class UnsatisfiableIterator implements Iterator {
        public UnsatisfiableIterator(Map map) {
        }
        public void remove() {
        }

        public boolean hasNext() {
            return false;
        }

        public Object next() {
            return null;
        }
    }

    public void testDelegationOfVerify() {
        DefaultPicoContainer mpc = new DefaultPicoContainer();
        mpc.registerComponentImplementation(Iterator.class, UnsatisfiableIterator.class);
        ImmutablePicoContainer ipc = new ImmutablePicoContainer(mpc);
        try {
            ipc.verify();
            fail("wrong!");
        } catch (PicoVerificationException e) {
            // expected
        }
    }

    public void testGetParentForMutable() {
        DefaultPicoContainer par = new DefaultPicoContainer();
        DefaultPicoContainer mpc = new DefaultPicoContainer(par);
        mpc.registerComponentImplementation(Map.class, HashMap.class);
        ImmutablePicoContainer ipc = new ImmutablePicoContainer(mpc);
        PicoContainer parent = ipc.getParent();
        assertNotNull(parent);
        assertNotSame(par, parent);
        PicoContainer parent2 = ipc.getParent();
        assertNotNull(parent2);
        assertEquals(parent, parent2);
    }

    public void testGetParentForNonMutable() {
        DefaultPicoContainer par = new DefaultPicoContainer();
        ImmutablePicoContainer par2 = new ImmutablePicoContainer(par);
        DefaultPicoContainer mpc = new DefaultPicoContainer(par2);
        mpc.registerComponentImplementation(Map.class, HashMap.class);
        ImmutablePicoContainer ipc = new ImmutablePicoContainer(mpc);
        PicoContainer parent = ipc.getParent();
        assertNotNull(parent);
        assertNotSame(par, parent);
        PicoContainer parent2 = ipc.getParent();
        assertNotNull(parent2);
        assertEquals(parent, parent2);
    }

    public void testStartBarfs() {
        DefaultPicoContainer mpc = new DefaultPicoContainer();
        mpc.registerComponentImplementation(Map.class, HashMap.class);
        ImmutablePicoContainer ipc = new ImmutablePicoContainer(mpc);
        try {
            ipc.start();
            fail("should have barfed");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
    public void testStopBarfs() {
        DefaultPicoContainer mpc = new DefaultPicoContainer();
        mpc.registerComponentImplementation(Map.class, HashMap.class);
        ImmutablePicoContainer ipc = new ImmutablePicoContainer(mpc);
        try {
            ipc.stop();
            fail("stop have barfed");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    public void testDisposeBarfs() {
        DefaultPicoContainer mpc = new DefaultPicoContainer();
        mpc.registerComponentImplementation(Map.class, HashMap.class);
        ImmutablePicoContainer ipc = new ImmutablePicoContainer(mpc);
        try {
            ipc.dispose();
            fail("should have barfed");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    public void testAddOrderedComponentAdapter() {
        DefaultPicoContainer mpc = new DefaultPicoContainer();
        mpc.registerComponentImplementation(Map.class, HashMap.class);
        ImmutablePicoContainer ipc = new ImmutablePicoContainer(mpc);
        try {
            ipc.addOrderedComponentAdapter(null);
            fail("should have barfed");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    public void testComponentAdaptersGetContainer() {
        DefaultPicoContainer mpc = new DefaultPicoContainer();
        mpc.registerComponentImplementation(Map.class, HashMap.class);
        ImmutablePicoContainer ipc = new ImmutablePicoContainer(mpc);
        ComponentAdapter ca = ipc.getComponentAdapter(Map.class);
        assertNotNull(ca);
        assertTrue(ca instanceof ImmutableComponentAdapter);
        PicoContainer container = ca.getContainer();
        PicoContainer container2 = ca.getContainer();
        assertTrue(container instanceof ImmutablePicoContainer);
        assertTrue(container2 instanceof ImmutablePicoContainer);
        assertSame(container, container2);

    }

    public void testHashCode() {
        DefaultPicoContainer mpc = new DefaultPicoContainer();
        mpc.registerComponentImplementation(Map.class, HashMap.class);
        ImmutablePicoContainer ipc = new ImmutablePicoContainer(mpc);
        assertNotSame(mpc,ipc);
        ImmutablePicoContainer ipc2 = new ImmutablePicoContainer(mpc);
        assertNotSame(ipc2,ipc);
        assertEquals(ipc2,ipc);
    }

}