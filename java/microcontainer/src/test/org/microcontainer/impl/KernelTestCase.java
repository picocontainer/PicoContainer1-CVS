/*****************************************************************************
 * Copyright (C) MicroContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 *****************************************************************************/

package org.microcontainer.impl;

import junit.framework.TestCase;
import org.microcontainer.Kernel;
import org.microcontainer.DeploymentException;
import org.picocontainer.defaults.DefaultPicoContainer;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.ZipException;

/**
 * @author Paul Hammant
 * @author Mike Ward
 * @version $Revision$
 */

public class KernelTestCase extends TestCase { // LSD: extends PicoTCKTestCase of some sort I'd hope
    private Kernel kernel;

    protected void setUp() throws Exception {
        super.setUp();

        kernel = new DefaultKernel();
    }

    public void testDeploymentOfMarFile() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, DeploymentException {
        kernel.deploy(new File("test.mar"));
        Object o = kernel.getComponent("test/org.microcontainer.test.TestComp");
        assertNotNull(o);
        Method m = o.getClass().getMethod("testMe", new Class[0]);
        assertEquals("hello", m.invoke(o, new Object[0]));
    }

    public void testDeploymentOfMarFileFromURL() throws Exception {
        //kernel.deploy(new URL("http://cvs.picocontainer.codehaus.org/java/microcontainer/src/remotecomp.mar"));
		File testMar = new File("test.mar");
		kernel.deploy(new URL("jar:file:" + testMar.getCanonicalPath() + "!/"));
        Object o = kernel.getComponent("test/org.microcontainer.test.TestComp");
        assertNotNull(o);
        Method m = o.getClass().getMethod("testMe", new Class[0]);
        assertEquals("hello", m.invoke(o, new Object[0]));
    }

    public void testDeferredDeploymentOfMarFile() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        kernel.deferredDeploy(new File("test.mar"));
        Object o = kernel.getComponent("test/org.microcontainer.test.TestComp");
        assertNull(o);
        kernel.start("test/org.microcontainer.test.TestComp");
        o = kernel.getComponent("test/org.microcontainer.test.TestComp");
        assertNotNull(o);
        Method m = o.getClass().getMethod("testMe", new Class[0]);
        assertEquals("hello", m.invoke(o, new Object[0]));
    }

    public void testDeployedMarsComponentsAreInDifferentClassloaderToKernel() throws DeploymentException{
        kernel.deploy(new File("test.mar"));
        Object o = kernel.getComponent("test/org.microcontainer.test.TestComp");
        assertNotNull(o);
		Class interfaceClass = o.getClass().getInterfaces()[0];
        // the interface should be two removed from each other.
        assertEquals(kernel.getClass().getClassLoader(), interfaceClass.getClassLoader().getParent().getParent());

        // LSD: what kind of number is that, "two"?
        // You're testing that the kernel is two classloaders
        // above the component, which is not
        // testDeployedMarsComponentsAreInDiffClassloaderToKernel()
    }

    public void testAPIisPromoted() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, DeploymentException {
        kernel.deploy(new File("test.mar"));
        Object o = kernel.getComponent("test/org.microcontainer.testapi.TestPromotable");
		Class interfaceClass =  o.getClass().getInterfaces()[0];

		// the interface's class loader should be one removed from the kernel's class loader
        assertEquals(kernel.getClass().getClassLoader(), interfaceClass.getClassLoader().getParent());
        Method m = o.getClass().getMethod("unHideImplClassLoader", new Class[0]);
        ClassLoader implClassLoader = (ClassLoader) m.invoke(o, new Class[0]);
        // these should be two removed from each other.
        assertEquals(kernel.getClass().getClassLoader(), implClassLoader.getParent().getParent());
        // LSD: those numbers again...you want to expose the classloader architecture
        // to the client...that'll make it difficult to change...
    }

    public void testTwoDeployedMarsComponentAPIsAreInDifferentClassloader() throws Exception {
		URL url = new URL("jar:file:test.mar!/");
        kernel.deploy("test", url);
        Object o = kernel.getComponent("test/org.microcontainer.test.TestComp");
		kernel.deploy("test2", url);
        Object o2 = kernel.getComponent("test2/org.microcontainer.test.TestComp");
        assertNotNull(o);
        assertNotNull(o2);
        // LSD: this, I like...
        assertNotSame(o.getClass().getClassLoader(), o2.getClass().getClassLoader());
    }

    public void testTwoDeployedMarsComponentImplementationsAreInDifferentClassloader() throws Exception {
        URL url = new URL("jar:file:test.mar!/");
        kernel.deploy("test", url);
        Object o = kernel.getComponent("test/org.microcontainer.test.TestComp");
        Method m = o.getClass().getMethod("unHideImplClassLoader", new Class[0]);
        kernel.deploy("test2", url);
        Object o2 = kernel.getComponent("test2/org.microcontainer.test.TestComp");

        // unHideImplClassLoader allows us to cater for the fact that the default behavior of Mega
        // is to hide implementations. The method would amount to a logical security flaw if
        // implemented in a real component.

        Method m2 = o2.getClass().getMethod("unHideImplClassLoader", new Class[0]);
        ClassLoader impl1ClassLoader = (ClassLoader) m.invoke(o, new Object[0]);
        ClassLoader impl2ClassLoader = (ClassLoader) m2.invoke(o2, new Object[0]);
        assertNotSame(impl1ClassLoader, impl2ClassLoader);
    }

    public void testKernelAndKernelImplAreIndifferentClassLoaders() {
        // might be quite hard
    }

    public void testMarWithUnsupportedNanoContainerScriptCannotBeDeployed() throws DeploymentException{
        kernel.deploy(new File("bogus.mar"));
        // .bogus language not supported
    }

    public void testMarMissingJavaCannotBeDeployed() throws DeploymentException{
        kernel.deploy(new File("incomplete.mar"));
        // .bogus language not supported
    }

    public void testDeploymentOfMarFileResultsInAProperExceptionOnMissingFile() throws DeploymentException {
		try {
			kernel.deploy(new File("missing.mar"));
			fail("DeploymentException should have been thrown");
		} catch (DeploymentException e) {
			assertTrue(e.getCause() instanceof ZipException);
		}
	}

    public void testDeploymentOfMarFileResultsInAProperExceptionOnBadURL() throws MalformedURLException, DeploymentException {
		try {
			kernel.deploy(new URL("http://cvs.picocontainer.codehaus.org/java/microcontainer/src/remotecomp.mar.badurl"));
			fail("DeploymentException should have been thrown");
		} catch (DeploymentException e) {
		}
	}

    public void testMarWithGroovyScriptErrorResultsInException() {
        // gracefully handle misconfiguration...
    }

    public void testMarFileAppCanBeStopped() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, DeploymentException {
        kernel.deploy(new File("test.mar"));
        Object o = kernel.getComponent("test/org.microcontainer.test.TestComp");
        Method m = o.getClass().getMethod("isRunning", new Class[0]);
        assertTrue(((Boolean) m.invoke(o, new Object[0])).booleanValue());
        kernel.stop("test");
        assertFalse(((Boolean) m.invoke(o, new Object[0])).booleanValue());
        // of course, any references to any comp will be usable prior to GC, even if the container has stopped.
    }

    public void testKernelImplIsInvisibleFromMarsSandbox() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, DeploymentException {

        // might not work as IDE is going to fight to keep these at VM classpath level

        kernel.deploy(new File("test.mar"));
        Object o = kernel.getComponent("test/org.microcontainer.test.TestComp");
        Method m = o.getClass().getMethod("testKernelImplIsInvisibleFromMarsSandbox", new Class[0]);
        m.invoke(o, new Object[0]); // asserts using Junit that certain classes do not exist in classpath
    }

    public void testExportedComponentsCanInteract()
    {
        // thomas' use case goes here
    }

    public void testHiddenStuffIsActuallyHidden()
    {
        // the comps are there, what's the test supposed to be?
    }

    public void testExportComponentsUsingAltRMI()
    {
        // what mechanism? Support one by default?
    }

    public void testBasicTreeNavigationUsingBasicXPath()
    {
        // not just opaque string handling pleez
    }

    public void testBasicTreeNavigationUsingComplexXPath()
    {
        // what about getting an array of components satisfying constraints?
        // XPath does that, no?
    }

    public void testMultipleKernelsPeaceFullyCoexistInAnEmbeddedEnvironment() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, DeploymentException
    {
        // was an issue with phoenix at times...ie these guys don't claim server sockets...
        Kernel kernel2 = new DefaultKernel();

        testDeploymentOfMarFile();

        kernel2.deploy(new File("test.mar"));
        Object o = kernel.getComponent("test/org.microcontainer.test.TestComp");
        assertNotNull(o);
        Method m = o.getClass().getMethod("testMe", new Class[0]);
        assertEquals("hello", m.invoke(o, new Object[0]));
    }

    public void testKernelCanRunInPicoContainer() {
        // lets keep true to COP principles, shall we?
        DefaultPicoContainer c = new DefaultPicoContainer();
        c.registerComponentImplementation(DefaultKernel.class);
        assertTrue( c.getComponentInstance(Kernel.class) instanceof Kernel );
        c.start();
        c.stop();
        c.dispose();
    }
}
