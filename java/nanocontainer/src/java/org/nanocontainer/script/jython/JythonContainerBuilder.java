/*****************************************************************************
 * Copyright (C) NanoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by                                                          *
 *****************************************************************************/
package org.nanocontainer.script.jython;

import org.nanocontainer.script.ScriptedComposingLifecycleContainerBuilder;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import org.python.util.PythonInterpreter;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * {@inheritDoc}
 * The script has to assign a "pico" variable with an instance of {@link PicoContainer}.
 * There is an implicit variable named "parent" that may contain a reference to a parent
 * container. It is recommended to use this as a constructor argument to the instantiated
 * PicoContainer.
 * @author Paul Hammant
 * @author Mike Royle
 * @author Aslak Helles&oslash;y
 * @version $Revision$
 */
public class JythonContainerBuilder extends ScriptedComposingLifecycleContainerBuilder {
    public JythonContainerBuilder(Reader script, ClassLoader classLoader) {
        super(script, classLoader);
    }

    protected MutablePicoContainer createContainer(PicoContainer parentContainer, Object assemblyScope) {
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.exec("from org.picocontainer.defaults import *");
        interpreter.exec("from org.nanocontainer.reflection import *");
        interpreter.exec("from java.net import *");
        interpreter.set("parent", parentContainer);
        interpreter.set("assemblyScope", assemblyScope);
        interpreter.execfile(new InputStream() {
            public int read() throws IOException {
                return script.read();
            }
        });
        return (MutablePicoContainer) interpreter.get("pico", MutablePicoContainer.class);
    }
}
