/*****************************************************************************
 * Copyright (Cc) PicoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Idea by Rachel Davies, Original code by Aslak Hellesoy and Paul Hammant   *
 *****************************************************************************/

package picocontainer.composite;

import junit.framework.TestCase;
import picocontainer.defaults.NullContainer;
import picocontainer.hierarchical.HierarchicalPicoContainer;
import picocontainer.testmodel.WilmaImpl;
import picocontainer.*;

public class CompositePicoContainerTestCase extends TestCase {
    private RegistrationPicoContainer pico;
    private CompositePicoContainer.Filter filter;

    public void setUp() throws PicoRegistrationException, PicoIntrospectionException {
        pico = new HierarchicalPicoContainer.Default();
        pico.registerComponentByClass(WilmaImpl.class);
        filter = new CompositePicoContainer.Filter(pico);
    }

    public void testGetComponents() {
        assertEquals("Content of Component arrays should be the same", pico, filter.getSubject());
    }

    public void testGetComponentTypes() {
        assertEquals("Content of Component type arrays should be the same", pico, filter.getSubject());
    }

    public void testGetComponent() {
        assertSame("Wilma should be the same", pico.getComponent(WilmaImpl.class), filter.getComponent(WilmaImpl.class));
    }

    public void testHasComponent() {
        assertEquals("Containers should contain the same", pico.hasComponent(WilmaImpl.class), filter.hasComponent(WilmaImpl.class));
    }

    public void testNullContainer() {
        try {
            new CompositePicoContainer.Filter(null);
            fail("Should have failed with an NPE");
        } catch (NullPointerException e) {
            // fine
        }
    }

    public void testNullArrayContainer() {
        try {
            new CompositePicoContainer(null);
            fail("Should have failed with an NPE");
        } catch (NullPointerException e) {
            // fine
        }
    }

    public void testGetToFilterFor() {
        assertSame("The PicoContainer to filter for should be the one made in setUp", pico, filter.getSubject());
    }

    public void testBasic() {

        final String acomp = "hello";
        final Integer bcomp = new Integer(123);

        PicoContainer a = new PicoContainer() {
            public boolean hasComponent(Object compKey) {
                return compKey == String.class;
            }

            public Object getComponent(Object compKey) {
                return compKey == String.class ? acomp : null;
            }

            public Object[] getComponents() {
                return new Object[]{acomp};
            }

            public Object[] getComponentKeys() {
                return new Class[]{String.class};
            }

            public void instantiateComponents() throws PicoInstantiationException {
            }

            public Object getCompositeComponent()
            {
                return null;
            }

            public Object getCompositeComponent(boolean callInInstantiationOrder, boolean callUnmanagedComponents)
            {
                return null;
            }
        };

        PicoContainer b = new PicoContainer() {
            public boolean hasComponent(Object compKey) {
                return compKey == Integer.class;
            }

            public Object getComponent(Object compKey) {
                return compKey == Integer.class ? bcomp : null;
            }

            public Object[] getComponents() {
                return new Object[]{bcomp};
            }

            public Object[] getComponentKeys() {
                return new Class[]{Integer.class};
            }

            public void instantiateComponents() throws PicoInstantiationException {
            }

            public Object getCompositeComponent()
            {
                return null;
            }

            public Object getCompositeComponent(boolean callInInstantiationOrder, boolean callUnmanagedComponents)
            {
                return null;
            }
        };

        CompositePicoContainer acc = new CompositePicoContainer(new PicoContainer[]{a, b});

        assertTrue(acc.hasComponent(String.class));
        assertTrue(acc.hasComponent(Integer.class));
        assertTrue(acc.getComponent(String.class) == acomp);
        assertTrue(acc.getComponent(Integer.class) == bcomp);
        assertTrue(acc.getComponents().length == 2);

    }

    public void testEmpty() {

        CompositePicoContainer acc = new CompositePicoContainer(new PicoContainer[0]);
        assertTrue(acc.hasComponent(String.class) == false);
        assertTrue(acc.getComponent(String.class) == null);
        assertTrue(acc.getComponents().length == 0);

    }

    public void testInstantiation() throws PicoInstantiationException
    {
        NullContainer nc = new NullContainer ();
        // Should not barf. Should do nothing, but that hard to test.
        nc.instantiateComponents();
    }
}