/*****************************************************************************
 * Copyright (C) PicoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by Leo Simmons & Joerg Schaible                             *
 *****************************************************************************/
package org.picocontainer.gems;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.defaults.DefaultPicoContainer;

import junit.framework.TestCase;

/**
 * @author J&ouml;rg Schaible
 */
public class StaticFactoryComponentAdapterTestCase
        extends TestCase {

    public void testStaticFactoryInAction() {
        ComponentAdapter componentAdapter = 
            new StaticFactoryComponentAdapter(Registry.class, 
                    new StaticFactory() {
                        public Object get() {
                            try {
                                return LocateRegistry.getRegistry();
                            } catch (RemoteException e) {
                                return null;
                            }
                        }
            });
        
        DefaultPicoContainer pico = new DefaultPicoContainer();
        pico.registerComponent(componentAdapter).verify(pico);
        Registry registry = (Registry)pico.getComponentInstance(Registry.class);
        assertNotNull(registry);
    }
}
