package org.picocontainer.tck;

import junit.framework.TestCase;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoInitializationException;
import org.picocontainer.PicoIntrospectionException;
import org.picocontainer.PicoRegistrationException;
import org.picocontainer.testmodel.DependsOnTouchable;
import org.picocontainer.testmodel.Touchable;
import org.picocontainer.defaults.UnsatisfiedDependencyInstantiationException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractBasicStringCompatabilityTestCase extends TestCase {

    abstract public PicoContainer createPicoContainerWithTouchableAndDependency() throws
            PicoRegistrationException, PicoIntrospectionException;

    abstract public PicoContainer createPicoContainerWithTouchablesDependancyOnly() throws
            PicoRegistrationException, PicoIntrospectionException;

    public void testNotNull() throws PicoRegistrationException, PicoIntrospectionException {
        assertNotNull("Are you calling super.setUp() in your setUp method?", createPicoContainerWithTouchableAndDependency());
    }

    public void testBasicInstantiationAndContainment() throws PicoInitializationException, PicoRegistrationException {
        PicoContainer picoContainer = createPicoContainerWithTouchableAndDependency();
        picoContainer.instantiateComponents();
        assertTrue("Container should have Touchable component",
                picoContainer.hasComponent("touchable"));
        assertTrue("Container should have DependsOnTouchable component",
                picoContainer.hasComponent("dependsOnTouchable"));
        assertTrue("Component should be instance of Touchable",
                picoContainer.getComponent("touchable") instanceof Touchable);
        assertTrue("Component should be instance of DependsOnTouchable",
                picoContainer.getComponent("dependsOnTouchable") instanceof DependsOnTouchable);
        assertTrue("should not have non existent component", !picoContainer.hasComponent("doesNotExist"));
    }

    protected abstract void addAHashMapByInstance(PicoContainer picoContainer) throws PicoRegistrationException, PicoIntrospectionException;

    public void testByInstanceRegistration() throws PicoRegistrationException, PicoInitializationException {
        PicoContainer picoContainer = createPicoContainerWithTouchableAndDependency();
        addAHashMapByInstance(picoContainer);
        picoContainer.instantiateComponents();
        assertEquals("Wrong number of comps in the internals", 3, picoContainer.getComponents().size());
        assertEquals("Key - 'map', Impl - HashMap should be in internals", HashMap.class, picoContainer.getComponent("map").getClass());
        //TODO - some way to test hashmap was passed in as an instance ?
        // should unmanaged side of DefaultPicoContainer be more exposed thru interface?
    }


}