/*****************************************************************************
 * Copyright (ComponentC) PicoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the license.html file.                                                    *
 *                                                                           *
 * Idea by Rachel Davies, Original code by Aslak Hellesoy and Paul Hammant   *
 *****************************************************************************/

package org.picocontainer.defaults;

import junit.framework.TestCase;
import org.picocontainer.lifecycle.Disposable;
import org.picocontainer.lifecycle.Startable;
import org.picocontainer.lifecycle.Stoppable;
import org.picocontainer.lifecycle.Lifecycle;
import org.picocontainer.testmodel.DependsOnTouchable;
import org.picocontainer.testmodel.SimpleTouchable;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * This class tests the lifecycle aspects of DefaultPicoContainer.
 */
public class DefaultPicoContainerLifecycleTestCase extends TestCase {

    public static class One implements Startable, Stoppable, Disposable {

        List instantiating = new ArrayList();
        List starting = new ArrayList();
        List stopping = new ArrayList();
        List disposing = new ArrayList();

        public One() {
            instantiation("One");
        }

        public void instantiation(String s) {
            instantiating.add(s);
        }

        public List getInstantiating() {
            return instantiating;
        }

        public List getStarting() {
            return starting;
        }

        public List getStopping() {
            return stopping;
        }

        public List getDisposing() {
            return disposing;
        }

        public void start() {
            startCalled("One");
        }

        public void stop() {
            stopCalled("One");
        }

        public void dispose() {
            disposeCalled("One");
        }

        public void startCalled(String msg) {
            starting.add(msg);
        }

        public void stopCalled(String msg) {
            stopping.add(msg);
        }

        public void disposeCalled(String msg) {
            disposing.add(msg);
        }

    }

    public static class Two implements Startable, Stoppable, Disposable {
        One one;

        public Two(One one) {
            one.instantiation("Two");
            this.one = one;
        }

        public void start() {
            one.startCalled("Two");
        }

        public void stop() {
            one.stopCalled("Two");
        }

        public void dispose() {
            one.disposeCalled("Two");
        }
    }

    public static class Three implements Startable, Stoppable, Disposable {
        One one;

        public Three(One one, Two two) {
            one.instantiation("Three");
            assertNotNull(two);
            this.one = one;
        }

        public void start() {
            one.startCalled("Three");
        }

        public void stop() {
            one.stopCalled("Three");
        }

        public void dispose() {
            one.disposeCalled("Three");
        }
    }

    public static class Four implements Startable, Stoppable, Disposable {
        One one;

        public Four(Two two, Three three, One one) {
            one.instantiation("Four");
            assertNotNull(two);
            assertNotNull(three);
            this.one = one;
        }

        public void start() {
            one.startCalled("Four");
        }

        public void stop() {
            one.stopCalled("Four");
        }

        public void dispose() {
            one.disposeCalled("Four");
        }
    }


    public void testOrderOfInstantiationWithoutAdapter() throws Exception {

        DefaultPicoContainer pico = new DefaultPicoContainer();

        pico.registerComponentImplementation(Four.class);
        pico.registerComponentImplementation(Two.class);
        pico.registerComponentImplementation(One.class);
        pico.registerComponentImplementation(Three.class);

        final List componentInstances = pico.getComponentInstances();
        assertEquals(4, componentInstances.size());

        // instantiation - would be difficult to do these in the wrong order!!
        assertEquals("Incorrect Order of Instantiation", One.class, componentInstances.get(0).getClass());
        assertEquals("Incorrect Order of Instantiation", Two.class, componentInstances.get(1).getClass());
        assertEquals("Incorrect Order of Instantiation", Three.class, componentInstances.get(2).getClass());
        assertEquals("Incorrect Order of Instantiation", Four.class, componentInstances.get(3).getClass());
    }

    public void testStartStopStartStopAndDispose() throws Exception {

        DefaultPicoContainer pico = new DefaultPicoContainer();

        pico.registerComponentImplementation(One.class);
        pico.registerComponentImplementation(Two.class);
        pico.registerComponentImplementation(Three.class);
        pico.registerComponentImplementation(Four.class);

        One one = (One) pico.getComponentInstance(One.class);

        pico.getComponentInstances();

        // instantiation - would be difficult to do these in the wrong order!!
        assertEquals("Should be four elems", 4, one.getInstantiating().size());
        assertEquals("Incorrect Order of Instantiation", "One", one.getInstantiating().get(0));
        assertEquals("Incorrect Order of Instantiation", "Two", one.getInstantiating().get(1));
        assertEquals("Incorrect Order of Instantiation", "Three", one.getInstantiating().get(2));
        assertEquals("Incorrect Order of Instantiation", "Four", one.getInstantiating().get(3));

        startStopDisposeLifecycleComps(pico, pico, pico, one);

    }

    private void startStopDisposeLifecycleComps(Startable start, Stoppable stop, Disposable disp, One one) throws Exception {
        start.start();

        // post instantiation startup
        assertEquals("Should be four elems", 4, one.getStarting().size());
        assertEquals("Incorrect Order of Starting", "One", one.getStarting().get(0));
        assertEquals("Incorrect Order of Starting", "Two", one.getStarting().get(1));
        assertEquals("Incorrect Order of Starting", "Three", one.getStarting().get(2));
        assertEquals("Incorrect Order of Starting", "Four", one.getStarting().get(3));

        stop.stop();

        // post instantiation shutdown - REVERSE order.
        assertEquals("Should be four elems", 4, one.getStopping().size());
        assertEquals("Incorrect Order of Stopping", "Four", one.getStopping().get(0));
        assertEquals("Incorrect Order of Stopping", "Three", one.getStopping().get(1));
        assertEquals("Incorrect Order of Stopping", "Two", one.getStopping().get(2));
        assertEquals("Incorrect Order of Stopping", "One", one.getStopping().get(3));

        disp.dispose();

        // post instantiation shutdown - REVERSE order.
        assertEquals("Should be four elems", 4, one.getDisposing().size());
        assertEquals("Incorrect Order of Stopping", "Four", one.getDisposing().get(0));
        assertEquals("Incorrect Order of Stopping", "Three", one.getDisposing().get(1));
        assertEquals("Incorrect Order of Stopping", "Two", one.getDisposing().get(2));
        assertEquals("Incorrect Order of Stopping", "One", one.getDisposing().get(3));
    }


    public void testStartStartCausingBarf() throws Exception {

        DefaultPicoContainer pico = new DefaultPicoContainer();

        pico.registerComponentImplementation(DependsOnTouchable.class);
        pico.registerComponentImplementation(SimpleTouchable.class);

        pico.getComponentInstances();

        assertTrue(pico.isStopped());
        pico.start();
        assertTrue(pico.isStarted());
        try {
            pico.start();
            fail("Should have barfed");
        } catch (IllegalStateException e) {
            // expected;
            assertTrue(pico.isStarted());
        }
    }

    public void testStartStopStopCausingBarf() throws Exception {
        DefaultPicoContainer pico = new DefaultPicoContainer();

        pico.registerComponentImplementation(DependsOnTouchable.class);
        pico.registerComponentImplementation(SimpleTouchable.class);

        pico.getComponentInstances();
        assertTrue(pico.isStopped());
        pico.start();
        assertTrue(pico.isStarted());
        pico.stop();
        assertTrue(pico.isStopped());
        try {
            pico.stop();
            fail("Should have barfed");
        } catch (IllegalStateException e) {
            // expected;
            assertTrue(pico.isStopped());
        }
    }

    public void testDisposeDisposeCausingBarf() throws Exception {
        DefaultPicoContainer pico = new DefaultPicoContainer();

        pico.registerComponentImplementation(DependsOnTouchable.class);
        pico.registerComponentImplementation(SimpleTouchable.class);

        pico.getComponentInstances();
        pico.start();
        pico.stop();
        assertFalse(pico.isDisposed());
        pico.dispose();
        assertTrue(pico.isDisposed());
        try {
            pico.dispose();
            fail("Should have barfed");
        } catch (IllegalStateException e) {
            // expected;
            assertTrue(pico.isDisposed());
        }
    }


    public void testStartStopDisposeDisposeCausingBarf() throws Exception {
        DefaultPicoContainer pico = new DefaultPicoContainer();

        pico.registerComponentImplementation(DependsOnTouchable.class);
        pico.registerComponentImplementation(SimpleTouchable.class);

        pico.getComponentInstances();
        pico.start();
        pico.stop();
        pico.dispose();
        try {
            pico.dispose();
            fail("Should have barfed");
        } catch (IllegalStateException e) {
            // expected;
        }
    }

    public static class FooRunnable implements Runnable, Startable, Stoppable {
        private int runCount;
        private Thread thread = new Thread();
        private boolean interrupted;

        public FooRunnable() {
        }

        public int runCount() {
            return runCount;
        }

        public boolean isInterrupted() {
            return interrupted;
        }

        public void start() {
            thread = new Thread(this);
            thread.start();
        }

        public void stop() {
            thread.interrupt();
        }

        // this would do something a bit more concrete
        // than counting in real life !
        public void run() {
            runCount++;
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                interrupted = true;
            }
        }
    }

    public void testStartStopOfDaemonizedThread() throws Exception {
        DefaultPicoContainer pico = new DefaultPicoContainer();
        LifecycleAdapter lifecycle = new LifecycleAdapter(pico);


        pico.registerComponentImplementation(DependsOnTouchable.class);
        pico.registerComponentImplementation(SimpleTouchable.class);
        pico.registerComponentImplementation(FooRunnable.class);

        pico.getComponentInstances();
        lifecycle.start();
        assertTrue(lifecycle.isStarted());
        Thread.sleep(100);
        lifecycle.stop();
        assertTrue(lifecycle.isStopped());

        FooRunnable foo = (FooRunnable) pico.getComponentInstance(FooRunnable.class);
        assertEquals(1, foo.runCount());
        lifecycle.start();
        assertTrue(lifecycle.isStarted());
        Thread.sleep(100);
        lifecycle.stop();
        assertTrue(lifecycle.isStopped());
        assertEquals(2, foo.runCount());

    }

    public void testForgivingNatureOfLifecycleAdapter() throws Exception {

        DefaultPicoContainer pico = new DefaultPicoContainer();
        LifecycleAdapter lifecycle = new LifecycleAdapter(pico);


        // Touchable is not Startable (etc). This internals should be able to handle the
        // fact that none of the comps are Startable (etc).
        pico.registerComponentImplementation(SimpleTouchable.class);

        pico.getComponentInstances();

        assertTrue(lifecycle.isStopped());
        lifecycle.start();
        assertTrue(lifecycle.isStarted());

    }


    // This is the basic functionality for starting of child containers

    public void testDefaultPicoContainerRegisteredAsComponentGetsHostingContainerAsParent() {
        MutablePicoContainer parent = new DefaultPicoContainer();
        parent.registerComponentImplementation("child", DefaultPicoContainer.class);
        PicoContainer child = (PicoContainer) parent.getComponentInstance("child");
        assertSame(parent, child.getParent());
    }

    public void testGetComponentInstancesOnParentContainerHostedChildContainerDoesntReturnParentAdapter() {
        MutablePicoContainer parent = new DefaultPicoContainer();
        parent.registerComponentImplementation("child", DefaultPicoContainer.class);
        DefaultPicoContainer child = (DefaultPicoContainer) parent.getComponentInstance("child");
        assertEquals(0, child.getComponentInstances().size());
    }

    public abstract static class RecordingLifecycle implements Lifecycle {
        private final StringBuffer recording;

        protected RecordingLifecycle(StringBuffer recording) {
            this.recording = recording;
        }

        public void start() {
            recording.append("<" + code());
        }

        public void stop() {
            recording.append(code() + ">");
        }

        public void dispose() {
            recording.append("<" + code());
        }

        private String code() {
            String name = getClass().getName();
            return name.substring(name.indexOf('$')+1);
        }
    }

    public static class A extends RecordingLifecycle {
        public A(StringBuffer recording) {
            super(recording);
        }
    }
    public static class B extends RecordingLifecycle {
        public B(StringBuffer recording) {
            super(recording);
        }
    }

    public void testContainersArePutLastAndTheOthersAreMaintainedInSamePlace() {
        List l = new ArrayList();
        l.add("ComponentC");
        l.add(new DefaultPicoContainer());
        l.add("ComponentB");
        l.add(new DefaultPicoContainer());
        l.add("A");
        l.add(new DefaultPicoContainer());
        l.add("ComponentD");
        Collections.sort(l,new StackContainersAtEndComparator());
        assertEquals("ComponentC", l.get(0));
        assertEquals("ComponentB", l.get(1));
        assertEquals("A", l.get(2));
        assertEquals("ComponentD", l.get(3));
    }

    public void testComponentsAreStartedBreadthFirstAndStoppedDepthFirst() {
        MutablePicoContainer parent = new DefaultPicoContainer();
        parent.registerComponentImplementation("recording", StringBuffer.class);
        parent.registerComponentImplementation(A.class);
        parent.registerComponentImplementation("child", DefaultPicoContainer.class);
        DefaultPicoContainer child = (DefaultPicoContainer) parent.getComponentInstance("child");
        child.registerComponentImplementation(B.class);
        assertFalse(child.isStarted());
        parent.start();
        assertTrue(child.isStarted());
        parent.stop();

        assertEquals("<A<BB>A>", parent.getComponentInstance("recording").toString());
    }
}