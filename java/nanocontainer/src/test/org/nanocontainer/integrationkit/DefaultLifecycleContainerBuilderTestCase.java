package org.picoextras.integrationkit;

import junit.framework.TestCase;
import org.jmock.C;
import org.jmock.Mock;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;
import org.picocontainer.defaults.ObjectReference;
import org.picocontainer.extras.DefaultLifecyclePicoAdapter;
import org.picocontainer.lifecycle.LifecyclePicoAdapter;

/**
 * @author Aslak Helles&oslash;y
 * @version $Revision$
 */
public class DefaultLifecycleContainerBuilderTestCase extends TestCase {
    private class SimpleObjectReference implements ObjectReference {
        private Object item;

        public Object get() {
            return item;
        }

        public void set(Object item) {
            this.item = item;
        }
    }

    public void testBuildContainerCreatesANewChildContainer() {
        DefaultLifecycleContainerBuilder builder = new DefaultLifecycleContainerBuilder();
        Mock containerAssembler = new Mock(ContainerAssembler.class);
        containerAssembler.expect("assembleContainer", C.ANY_ARGS);

        SimpleObjectReference parentRef = new SimpleObjectReference();
        MutablePicoContainer parentC = new DefaultPicoContainer();
        DefaultLifecyclePicoAdapter parentCLifecycle = new DefaultLifecyclePicoAdapter(parentC);
        parentRef.set(parentCLifecycle);

        SimpleObjectReference childRef = new SimpleObjectReference();

        builder.buildContainer(childRef, parentRef, (ContainerAssembler) containerAssembler.proxy(), "test");
        LifecyclePicoAdapter childContainerLifecycle = (LifecyclePicoAdapter) childRef.get();
        PicoContainer childContainer = childContainerLifecycle.getPicoContainer();
        assertTrue(childContainer.getParentContainers().contains(parentC));

        builder.killContainer(childRef);
        assertFalse(childContainer.getParentContainers().contains(parentC));
    }

}