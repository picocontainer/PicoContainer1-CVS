/*****************************************************************************
 * Copyright (C) PicoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Idea by Rachel Davies, Original code by Paul Hammant                      *
 *****************************************************************************/

package org.picocontainer.extras;

import org.picocontainer.defaults.DefaultPicoContainer;
import junit.framework.TestCase;

public class ThrowawayPicoInstantiatorTestCase extends TestCase {

    public void testInstantiation() {

        DefaultPicoContainer dpc = new DefaultPicoContainer();
        dpc.registerComponentImplementation(Foo.class);

        ThrowawayPicoInstantiator tpc = new ThrowawayPicoInstantiator(dpc, Bar.class);
        Object instance = tpc.getInstance();
        System.out.println("--> " + instance + " " + instance.getClass());
        assertNotNull(instance);
        assertTrue(instance instanceof Bar);
    }

    public static class Foo {
        public Foo() {
        }
    }
    public static class Bar {
        public Bar(Foo foo) {
        }
    }

}