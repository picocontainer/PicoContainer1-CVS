package org.nanocontainer.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.picocontainer.extras.InvokingComponentAdapterFactory;
import org.picocontainer.extras.BeanPropertyComponentAdapterFactory;
import org.picocontainer.defaults.*;
import org.picocontainer.Parameter;
import org.picocontainer.MutablePicoContainer;

import java.util.*;

/**
 * An Ant task that makes the use of PicoContainer possible from Ant.
 * When the task is executed, it will invoke <code>execute()</code>
 * on all components that have a public no-arg, non-static execute method.
 * The components's execute() method (if it exists) will be invoked
 * in the order of instantiation.
 *
 * &lt;taskdef name="pico" classname="org.nanocontainer.ant.PicoContainerTask"/&gt;
 *
 * &lt;pico&gt;
 *    &lt;component classname="foo.Bar" someprop="somevalue"/&gt;
 *    &lt;component classname="ping.Pong"/&gt;
 * &lt;/pico&gt;
 *
 * Also note that bean/ant style properties can be set too. The above
 * usage will call <code>setSomeprop("somevalue")</code> on the
 * <code>foo.Bar</code> instance.
 *
 * @author Aslak Helles&oslash;y
 * @version $Revision$
 */
public class PicoContainerTask extends Task {
    protected MutablePicoContainer createPicoContainer(InvokingComponentAdapterFactory invokingFactory) {
        return new DefaultPicoContainer(invokingFactory);
    }

    private final List components = new ArrayList();

    private final BeanPropertyComponentAdapterFactory propertyFactory;
    private final InvokingComponentAdapterFactory invokingFactory;
    private final MutablePicoContainer pico;

    public PicoContainerTask() {
        DefaultComponentAdapterFactory defaultFactory = new DefaultComponentAdapterFactory();
        propertyFactory = new BeanPropertyComponentAdapterFactory(defaultFactory);
        invokingFactory = new InvokingComponentAdapterFactory(
                    propertyFactory,
                    "execute",
                    null,
                    null
            );
        pico = createPicoContainer(invokingFactory);
    }

    public void addComponent(Component component) {
        components.add(component);
    }

    public final void execute() {
        registerComponentsSpecifiedInAnt();
        try {
            getPicoContainer().getComponentInstances();
        } catch (Exception e) {
            throw new BuildException(e);
        }
    }

    private void registerComponentsSpecifiedInAnt() {

        for (Iterator iterator = components.iterator(); iterator.hasNext();) {
            Component component = (Component) iterator.next();

            // set the properties on the adapter factory
            // they will be set upon instantiation
            Object key = component.getKey();
            Map properties = component.getProperties();
            propertyFactory.setProperties(key, properties);

            Parameter[] parameters = component.getParameters();

            try {
                Class aClass = getClassLoader().loadClass(component.getClassname());
                MutablePicoContainer picoContainer = (MutablePicoContainer) getPicoContainer();
                if (parameters != null) {
                    picoContainer.registerComponentImplementation(component.getKey(), aClass, parameters);
                } else {
                    picoContainer.registerComponentImplementation(component.getKey(), aClass);
                }
            } catch (Exception e) {
                throw new BuildException(e);
            }
        }
    }

    private ClassLoader getClassLoader() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = getClass().getClassLoader();
        }
        return classLoader;
    }

    public static class ExecutingComponentAdapterFactory extends InvokingComponentAdapterFactory {
        public ExecutingComponentAdapterFactory(ComponentAdapterFactory delegate) {
            super(delegate, "execute", null, null);
        }
    }

    public MutablePicoContainer getPicoContainer() {
        return pico;
    }
}