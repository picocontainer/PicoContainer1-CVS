package org.picocontainer.defaults;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.JVM;
import com.thoughtworks.xstream.io.xml.XppDriver;
import junit.framework.TestCase;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import org.picocontainer.testmodel.DependsOnTouchable;
import org.picocontainer.testmodel.SimpleTouchable;

/**
 * @author Aslak Helles&oslash;y
 * @version $Revision$
 */
public class XStreamSerialisationTestCase extends TestCase {
    private XStream xStream = new XStream(new XppDriver());

    public void testShouldBeAbleToSerialiseEmptyPico() {
        if (JVM.is14()) {
            MutablePicoContainer pico = new DefaultPicoContainer();
            String picoXml = xStream.toXML(pico);
            PicoContainer serializedPico = (PicoContainer) xStream.fromXML(picoXml);

            assertEquals(0, serializedPico.getComponentInstances().size());
        }
    }

    public void testShouldBeAbleToSerialisePicoWithUninstantiatedComponents() {
        if (JVM.is14()) {
            MutablePicoContainer pico = new DefaultPicoContainer();
            pico.registerComponentImplementation(SimpleTouchable.class);
            pico.registerComponentImplementation(DependsOnTouchable.class);
            String picoXml = xStream.toXML(pico);
            PicoContainer serializedPico = (PicoContainer) xStream.fromXML(picoXml);

            assertEquals(2, serializedPico.getComponentInstances().size());
        }
    }

    public void testShouldBeAbleToSerialisePicoWithInstantiatedComponents() {
        if (JVM.is14()) {
            MutablePicoContainer pico = new DefaultPicoContainer();
            pico.registerComponentImplementation(SimpleTouchable.class);
            pico.registerComponentImplementation(DependsOnTouchable.class);
            pico.getComponentInstances();
            String picoXml = xStream.toXML(pico);
            PicoContainer serializedPico = (PicoContainer) xStream.fromXML(picoXml);

            assertEquals(2, serializedPico.getComponentInstances().size());
        }
    }
}