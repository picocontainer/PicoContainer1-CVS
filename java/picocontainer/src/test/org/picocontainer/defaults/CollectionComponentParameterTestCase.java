package org.picocontainer.defaults;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Parameter;
import org.picocontainer.PicoContainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * @author Aslak Helles&oslash;y
 * @author J&ouml;rg Schaible
 * @version $Revision$
 */
public class CollectionComponentParameterTestCase
        extends MockObjectTestCase {

    public void testShouldInstantiateArrayOfStrings() {
        CollectionComponentParameter ccp = new CollectionComponentParameter();

        Mock componentAdapterMock = mock(ComponentAdapter.class);
        componentAdapterMock.expects(atLeastOnce()).method("getComponentKey").will(returnValue("x"));
        Mock containerMock = mock(PicoContainer.class);
        containerMock.expects(once()).method("getComponentAdaptersOfType").with(eq(String.class)).will(
                returnValue(Arrays.asList(new ComponentAdapter[]{
                        new InstanceComponentAdapter("y", "Hello"), new InstanceComponentAdapter("z", "World")})));
        containerMock.expects(once()).method("getParent").withNoArguments().will(returnValue(null));

        List expected = Arrays.asList(new String[]{"Hello", "World"});
        Collections.sort(expected);
        List actual = Arrays.asList((Object[]) ccp.resolveInstance(
                (PicoContainer) containerMock.proxy(), (ComponentAdapter) componentAdapterMock.proxy(), String[].class));
        Collections.sort(actual);
        assertEquals(expected, actual);
    }

    static public interface Fish {
    }

    static public class Cod
            implements Fish {
        public String toString() {
            return "Cod";
        }
    }

    static public class Shark
            implements Fish {
        public String toString() {
            return "Shark";
        }
    }

    static public class Bowl {
        private final Cod[] cods;
        private final Fish[] fishes;

        public Bowl(Cod cods[], Fish fishes[]) {
            this.cods = cods;
            this.fishes = fishes;
        }

        public Bowl(Collection cods, Collection fishes) {
            this.cods = (Cod[]) cods.toArray(new Cod[cods.size()]);
            this.fishes = (Fish[]) fishes.toArray(new Fish[fishes.size()]);
        }

        public Bowl(Cod cod, Map map) {
            this.cods = new Cod[]{cod};
            Collection collection = new ArrayList();
            for (final Iterator iter = map.entrySet().iterator(); iter.hasNext();) {
                final Map.Entry entry = (Map.Entry) iter.next();
                collection.add(entry.getValue());
            }
            this.fishes = (Fish[]) collection.toArray(new Fish[collection.size()]);
        }
    }

    static public class CollectedBowl {
        private final Cod[] cods;
        private final Fish[] fishes;

        public CollectedBowl(Collection cods, Collection fishes) {
            this.cods = (Cod[]) cods.toArray(new Cod[cods.size()]);
            this.fishes = (Fish[]) fishes.toArray(new Fish[fishes.size()]);
        }
    }

    static public class MappedBowl {
        private final Fish[] fishes;

        public MappedBowl(Map map) {
            Collection collection = new ArrayList();
            for (final Iterator iter = map.entrySet().iterator(); iter.hasNext();) {
                final Map.Entry entry = (Map.Entry) iter.next();
                collection.add(entry.getValue());
            }
            this.fishes = (Fish[]) collection.toArray(new Fish[collection.size()]);
        }
    }

    public void testNativeArrays() {
        MutablePicoContainer mpc = new DefaultPicoContainer();
        mpc.registerComponentImplementation(Bowl.class);
        mpc.registerComponentImplementation(Cod.class);
        mpc.registerComponentImplementation(Shark.class);
        Cod cod = (Cod) mpc.getComponentInstanceOfType(Cod.class);
        Bowl bowl = (Bowl) mpc.getComponentInstance(Bowl.class);
        assertEquals(1, bowl.cods.length);
        assertEquals(2, bowl.fishes.length);
        assertSame(cod, bowl.cods[0]);
        assertNotSame(bowl.fishes[0], bowl.fishes[1]);
    }

    public void testCollectionsAreGeneratedOnTheFly() {
        MutablePicoContainer mpc = new DefaultPicoContainer();
        mpc.registerComponent(new ConstructorInjectionComponentAdapter(Bowl.class, Bowl.class));
        mpc.registerComponentImplementation(Cod.class);
        Bowl bowl = (Bowl) mpc.getComponentInstance(Bowl.class);
        assertEquals(1, bowl.cods.length);
        mpc.registerComponentInstance("Nemo", new Cod());
        bowl = (Bowl) mpc.getComponentInstance(Bowl.class);
        assertEquals(2, bowl.cods.length);
        assertNotSame(bowl.cods[0], bowl.cods[1]);
    }

    public void testCollections() {
        MutablePicoContainer mpc = new DefaultPicoContainer();
        mpc.registerComponentImplementation(CollectedBowl.class, CollectedBowl.class, new Parameter[]{
                new ComponentParameter(Cod.class, false), new ComponentParameter(Fish.class, false)});
        mpc.registerComponentImplementation(Cod.class);
        mpc.registerComponentImplementation(Shark.class);
        Cod cod = (Cod) mpc.getComponentInstanceOfType(Cod.class);
        CollectedBowl bowl = (CollectedBowl) mpc.getComponentInstance(CollectedBowl.class);
        assertEquals(1, bowl.cods.length);
        assertEquals(2, bowl.fishes.length);
        assertSame(cod, bowl.cods[0]);
        assertNotSame(bowl.fishes[0], bowl.fishes[1]);
    }

    public void testMaps() {
        MutablePicoContainer mpc = new DefaultPicoContainer();
        mpc.registerComponentImplementation(MappedBowl.class, MappedBowl.class, new Parameter[]{new ComponentParameter(
                Fish.class, false)});
        mpc.registerComponentImplementation(Cod.class);
        mpc.registerComponentImplementation(Shark.class);
        MappedBowl bowl = (MappedBowl) mpc.getComponentInstance(MappedBowl.class);
        assertEquals(2, bowl.fishes.length);
        assertNotSame(bowl.fishes[0], bowl.fishes[1]);
    }
}