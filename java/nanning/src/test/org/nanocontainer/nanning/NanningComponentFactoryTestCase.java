/*****************************************************************************
 * Copyright (C) NanoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by Jon Tirs?n                                               *
 *****************************************************************************/

package org.picoextras.nanning;

import junit.framework.TestCase;
import org.codehaus.nanning.Aspects;
import org.codehaus.nanning.Invocation;
import org.codehaus.nanning.MethodInterceptor;
import org.codehaus.nanning.config.AspectSystem;
import org.codehaus.nanning.config.InterceptorAspect;
import org.picocontainer.*;
import org.picocontainer.defaults.*;

/**
 * @author Jon Tirsen
 * @version $Revision$
 */
public class NanningComponentFactoryTestCase extends TestCase {

    public interface Wilma {
        void hello();
    }

    public static class WilmaImpl implements Wilma {
        public void hello() {
        }
    }

    public static class FredImpl {
        public FredImpl(Wilma wilma) {
            assertNotNull("Wilma cannot be passed in as null", wilma);
            wilma.hello();
        }
    }

    private StringBuffer log = new StringBuffer();

    public void testComponentsWithOneInterfaceAreAspected() throws PicoInitializationException, AssignabilityRegistrationException, NotConcreteRegistrationException, PicoIntrospectionException {
        NanningComponentAdapter componentFactory =
                new NanningComponentAdapter(new AspectSystem(), new DefaultComponentAdapter(Wilma.class, WilmaImpl.class));
        Object component = componentFactory.getComponentInstance(new DefaultPicoContainer());
        assertTrue(Aspects.isAspectObject(component));
        assertEquals(Wilma.class, Aspects.getAspectInstance(component).getClassIdentifier());
    }

    public void testComponentsWithoutInterfaceNotAspected() throws PicoInitializationException, PicoRegistrationException {
        NanningComponentAdapter componentFactory = new NanningComponentAdapter(new AspectSystem(),
                new DefaultComponentAdapter(FredImpl.class, FredImpl.class));
        DefaultPicoContainer registry = new DefaultPicoContainer();
        registry.registerComponent(new InstanceComponentAdapter(Wilma.class, new WilmaImpl()));
        Object component = componentFactory.getComponentInstance(registry);
        assertFalse(Aspects.isAspectObject(component));
    }


    /**
     * Acceptance test (ie a teeny bit functional, but you'll get over it).
     */
    public void testSimpleLogOfMethodCall()
            throws PicoException, PicoInitializationException {

        AspectSystem aspectSystem = new AspectSystem();
        aspectSystem.addAspect(new InterceptorAspect(new MethodInterceptor() {
            public Object invoke(Invocation invocation) throws Throwable {
                log.append(invocation.getMethod().getName() + " ");
                return invocation.invokeNext();
            }
        }));

        MutablePicoContainer nanningEnabledPicoContainer = new DefaultPicoContainer(
                new NanningComponentAdapterFactory(aspectSystem, new DefaultComponentAdapterFactory()));
        nanningEnabledPicoContainer.registerComponentImplementation(Wilma.class, WilmaImpl.class);
        nanningEnabledPicoContainer.registerComponentImplementation(FredImpl.class);

        assertEquals("", log.toString());

        nanningEnabledPicoContainer.getComponentInstances();

        // fred says hello to wilma, even the interceptor knows
        assertEquals("hello ", log.toString());

        Wilma wilma = (Wilma) nanningEnabledPicoContainer.getComponentInstance(Wilma.class);

        assertNotNull(wilma);

        wilma.hello();

        // another entry in the log
        assertEquals("hello hello ", log.toString());

    }
}