/*****************************************************************************
 * Copyright (C) PicoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Idea by Rachel Davies, Original code by Aslak Hellesoy and Paul Hammant   *
 *****************************************************************************/

package org.picocontainer.defaults;

import junit.framework.TestCase;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoInitializationException;
import org.picocontainer.PicoInstantiationException;
import org.picocontainer.PicoIntrospectionException;
import org.picocontainer.internals.ComponentSpecification;
import org.picocontainer.composite.CompositePicoContainer;

import java.lang.reflect.InvocationTargetException;

public class DummiesTestCase extends TestCase {

    public void testDummyContainer() throws PicoInstantiationException {
        NullContainer dc = new NullContainer();
        dc.instantiateComponents();
        assertFalse(dc.hasComponent(String.class));
        assertNull(dc.getComponent(String.class));
        assertEquals(0, dc.getComponents().size());
        assertEquals(0, dc.getComponentKeys().size());
        assertNull(dc.getCompositeComponent());
        assertNull(dc.getCompositeComponent(true, false));
        assertNull(dc.getCompositeComponent(false, true));
    }

    public void testDefaultComponentFactory() throws PicoInstantiationException,
            NoSuchMethodException,
            InvocationTargetException,
            IllegalAccessException, PicoIntrospectionException {
        DefaultComponentFactory dcd = new DefaultComponentFactory();
        Object decorated = dcd.createComponent(new ComponentSpecification(dcd, Object.class, Object.class), null);
        assertNotNull(decorated);
    }

    public void donot_testInstantiation() throws PicoInitializationException {
        CompositePicoContainer acc = new CompositePicoContainer.WithContainerArray(new PicoContainer[0]);
        // Should not barf. Should do nothing, but that hard to test.
        // Hmmm, should it be silent, ot barf ?
        acc.instantiateComponents();
    }
}