/*****************************************************************************
 * Copyright (c) PicoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Idea by Rachel Davies, Original code by various                           *
 *****************************************************************************/
package org.nanocontainer.nanoaop.dynaop;

import java.lang.reflect.Method;

import org.nanocontainer.nanoaop.AbstractNanoaopTestCase;
import org.nanocontainer.nanoaop.AnotherInterface;
import org.nanocontainer.nanoaop.AspectsManager;
import org.nanocontainer.nanoaop.ClassPointcut;
import org.nanocontainer.nanoaop.ComponentPointcut;
import org.nanocontainer.nanoaop.Dao;
import org.nanocontainer.nanoaop.DaoImpl;
import org.nanocontainer.nanoaop.IdGenerator;
import org.nanocontainer.nanoaop.IdGeneratorImpl;
import org.nanocontainer.nanoaop.Identifiable;
import org.nanocontainer.nanoaop.IdentifiableMixin;
import org.nanocontainer.nanoaop.LoggingInterceptor;
import org.nanocontainer.nanoaop.MethodPointcut;
import org.nanocontainer.nanoaop.OrderEntity;
import org.nanocontainer.nanoaop.OrderEntityImpl;
import org.nanocontainer.nanoaop.PointcutsFactory;
import org.nanocontainer.nanoaop.defaults.AspectsComponentAdapterFactory;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.ComponentAdapterFactory;
import org.picocontainer.defaults.DefaultPicoContainer;

/**
 * @author Stephen Molitor
 */
public class DynaopAspectsManagerTestCase extends AbstractNanoaopTestCase {

    private AspectsManager aspects = new DynaopAspectsManager();
    private ComponentAdapterFactory caFactory = new AspectsComponentAdapterFactory(aspects);
    private MutablePicoContainer pico = new DefaultPicoContainer(caFactory);
    private PointcutsFactory cuts = aspects.getPointcutsFactory();

    public void testInterceptor() {
        StringBuffer log = new StringBuffer();
        aspects.registerInterceptor(cuts.instancesOf(Dao.class), cuts.allMethods(), new LoggingInterceptor(log));
        pico.registerComponentImplementation(Dao.class, DaoImpl.class);
        Dao dao = (Dao) pico.getComponentInstance(Dao.class);
        verifyIntercepted(dao, log);
    }

    public void testContainerSuppliedInterceptor() {
        aspects.registerInterceptor(cuts.instancesOf(Dao.class), cuts.allMethods(), LoggingInterceptor.class);

        pico.registerComponentImplementation("log", StringBuffer.class);
        pico.registerComponentImplementation(LoggingInterceptor.class);
        pico.registerComponentImplementation(Dao.class, DaoImpl.class);

        Dao dao = (Dao) pico.getComponentInstance(Dao.class);
        StringBuffer log = (StringBuffer) pico.getComponentInstance("log");
        verifyIntercepted(dao, log);
    }

    public void testComponentInterceptor() {
        StringBuffer log = new StringBuffer();

        aspects.registerInterceptor(cuts.component("intercepted"), cuts.allMethods(), new LoggingInterceptor(log));
        pico.registerComponentImplementation("intercepted", DaoImpl.class);
        pico.registerComponentImplementation("notIntercepted", DaoImpl.class);

        Dao intercepted = (Dao) pico.getComponentInstance("intercepted");
        Dao notIntercepted = (Dao) pico.getComponentInstance("notIntercepted");

        verifyIntercepted(intercepted, log);
        verifyNotIntercepted(notIntercepted, log);
    }

    public void testContainerSuppliedComponentInterceptor() {
        aspects.registerInterceptor(cuts.component("intercepted"), cuts.allMethods(), LoggingInterceptor.class);

        pico.registerComponentImplementation("log", StringBuffer.class);
        pico.registerComponentImplementation(LoggingInterceptor.class);
        pico.registerComponentImplementation("intercepted", DaoImpl.class);
        pico.registerComponentImplementation("notIntercepted", DaoImpl.class);

        StringBuffer log = (StringBuffer) pico.getComponentInstance("log");
        Dao intercepted = (Dao) pico.getComponentInstance("intercepted");
        Dao notIntercepted = (Dao) pico.getComponentInstance("notIntercepted");

        verifyIntercepted(intercepted, log);
        verifyNotIntercepted(notIntercepted, log);
    }

    public void testMixin() {
        aspects.registerMixin(cuts.instancesOf(Dao.class), IdentifiableMixin.class);
        pico.registerComponentImplementation(Dao.class, DaoImpl.class);
        Dao dao = (Dao) pico.getComponentInstance(Dao.class);
        verifyMixin(dao);
        assertTrue(dao instanceof AnotherInterface);
    }

    public void testContainerSuppliedMixin() {
        aspects.registerMixin(cuts.instancesOf(OrderEntity.class), IdentifiableMixin.class);
        pico.registerComponentImplementation("order1", OrderEntityImpl.class);
        pico.registerComponentImplementation("order2", OrderEntityImpl.class);

        // register mixin dependency:
        pico.registerComponentImplementation(IdGenerator.class, IdGeneratorImpl.class);

        Identifiable order1 = (Identifiable) pico.getComponentInstance("order1");
        Identifiable order2 = (Identifiable) pico.getComponentInstance("order2");

        assertEquals(new Integer(1), order1.getId());
        assertEquals(new Integer(2), order2.getId());

        // order1 and order2 do NOT share the same mixin instance (usually a
        // good thing),
        // although their mixin instances do share the same IdGenerator
        order1.setId(new Integer(42));
        assertEquals(new Integer(42), order1.getId());
        assertEquals(new Integer(2), order2.getId());
    }

    public void testContainerSuppliedMixinWithMixinExplicitlyRegistered() {
        aspects.registerMixin(cuts.instancesOf(OrderEntity.class), IdentifiableMixin.class);
        pico.registerComponentImplementation(IdentifiableMixin.class);
        pico.registerComponentImplementation("order1", OrderEntityImpl.class);
        pico.registerComponentImplementation("order2", OrderEntityImpl.class);

        Identifiable order1 = (Identifiable) pico.getComponentInstance("order1");
        Identifiable order2 = (Identifiable) pico.getComponentInstance("order2");

        assertEquals(new Integer(1), order1.getId());
        assertEquals(new Integer(1), order2.getId());

        // order1 and order2 share the same IdentifiableMixin object (not
        // usually what you want!)
        order1.setId(new Integer(42));
        assertEquals(new Integer(42), order1.getId());
        assertEquals(new Integer(42), order2.getId());
    }

    public void testComponentMixin() {
        pico.registerComponentImplementation("hasMixin", DaoImpl.class);
        pico.registerComponentImplementation("noMixin", DaoImpl.class);

        aspects.registerMixin(cuts.component("hasMixin"), IdentifiableMixin.class);

        Dao hasMixin = (Dao) pico.getComponentInstance("hasMixin");
        Dao noMixin = (Dao) pico.getComponentInstance("noMixin");

        verifyMixin(hasMixin);
        verifyNoMixin(noMixin);
        assertTrue(hasMixin instanceof AnotherInterface);
    }

    public void testContainerSuppliedComponentMixin() {
        aspects.registerMixin(cuts.componentName("hasMixin*"), new Class[] { Identifiable.class },
                IdentifiableMixin.class);

        pico.registerComponentImplementation("hasMixin1", OrderEntityImpl.class);
        pico.registerComponentImplementation("hasMixin2", OrderEntityImpl.class);
        pico.registerComponentImplementation("noMixin", OrderEntityImpl.class);
        pico.registerComponentImplementation(IdGenerator.class, IdGeneratorImpl.class);
        
        Identifiable hasMixin1 = (Identifiable) pico.getComponentInstance("hasMixin1");
        Identifiable hasMixin2 = (Identifiable) pico.getComponentInstance("hasMixin1");
        OrderEntity noMixin = (OrderEntity) pico.getComponentInstance("noMixin");
        
        assertFalse(noMixin instanceof Identifiable);
        assertEquals(new Integer(1), hasMixin1.getId());
        assertEquals(new Integer(2), hasMixin2.getId());
        
        hasMixin1.setId(new Integer(42));
        assertEquals(new Integer(42), hasMixin1.getId());
        assertEquals(new Integer(2), hasMixin2.getId());
    }

    public void testMixinExplicitInterfaces() {
        aspects.registerMixin(cuts.instancesOf(Dao.class), new Class[] { Identifiable.class }, IdentifiableMixin.class);
        pico.registerComponentImplementation(Dao.class, DaoImpl.class);
        Dao dao = (Dao) pico.getComponentInstance(Dao.class);
        verifyMixin(dao);
        assertFalse(dao instanceof AnotherInterface);
    }

    public void testComponentMixinExplicitInterfaces() {
        pico.registerComponentImplementation("hasMixin", DaoImpl.class);
        pico.registerComponentImplementation("noMixin", DaoImpl.class);

        aspects.registerMixin(cuts.component("hasMixin"), new Class[] { Identifiable.class }, IdentifiableMixin.class);

        Dao hasMixin = (Dao) pico.getComponentInstance("hasMixin");
        Dao noMixin = (Dao) pico.getComponentInstance("noMixin");

        verifyMixin(hasMixin);
        verifyNoMixin(noMixin);

        assertFalse(hasMixin instanceof AnotherInterface);
    }

    public void testCustomClassPointcut() {
        StringBuffer log = new StringBuffer();

        ClassPointcut customCut = new ClassPointcut() {
            public boolean picks(Class clazz) {
                return true;
            }
        };

        aspects.registerInterceptor(customCut, cuts.allMethods(), new LoggingInterceptor(log));
        pico.registerComponentImplementation(Dao.class, DaoImpl.class);
        Dao dao = (Dao) pico.getComponentInstance(Dao.class);
        verifyIntercepted(dao, log);
    }

    public void testCustomMethodPointcut() {
        StringBuffer log = new StringBuffer();

        MethodPointcut customCut = new MethodPointcut() {
            public boolean picks(Method method) {
                return true;
            }
        };

        aspects.registerInterceptor(cuts.instancesOf(Dao.class), customCut, new LoggingInterceptor(log));
        pico.registerComponentImplementation(Dao.class, DaoImpl.class);
        Dao dao = (Dao) pico.getComponentInstance(Dao.class);
        verifyIntercepted(dao, log);
    }

    public void testCustomComponentPointcut() {
        StringBuffer log = new StringBuffer();

        ComponentPointcut customCut = new ComponentPointcut() {
            public boolean picks(Object componentKey) {
                return true;
            }
        };

        aspects.registerInterceptor(customCut, cuts.allMethods(), new LoggingInterceptor(log));
        pico.registerComponentImplementation(Dao.class, DaoImpl.class);
        Dao dao = (Dao) pico.getComponentInstance(Dao.class);
        verifyIntercepted(dao, log);
    }

}