/*****************************************************************************
 * Copyright (C) NanoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by Paul Hammant                                             *
 *****************************************************************************/

package org.nanocontainer.reflection;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoException;
import org.picocontainer.MutablePicoContainer;
import org.nanocontainer.SoftCompositionPicoContainer;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Collections;

/**
 * A base class for SoftCompositionPicoContainers. As well as the functionality indicated by the interface it
 * implements, extenders of this class will have named child component capability.
 * @author Paul Hammant
 * @version $Revision$
 */
public abstract class AbstractSoftCompositionPicoContainer implements SoftCompositionPicoContainer {

    public final Object getComponentInstance(Object componentKey) throws PicoException {

        Object instance = getComponentInstanceFromDelegate(componentKey);

        if (instance != null) {
            return instance;
        }

        ComponentAdapter componentAdapter = null;
        if (componentKey.toString().startsWith("*")) {
            String candidateClassName = componentKey.toString().substring(1);
            Collection cas = getComponentAdapters();
            for (Iterator it = cas.iterator(); it.hasNext();) {
                ComponentAdapter ca = (ComponentAdapter) it.next();
                Object key = ca.getComponentKey();
                if (key instanceof Class && candidateClassName.equals(((Class) key).getName())) {
                    componentAdapter = ca;
                    break;
                }
            }
        }
        if (componentAdapter != null) {
            return componentAdapter.getComponentInstance();
        } else {
            return getComponentInstanceFromChildren(componentKey);
        }
    }

    private final Object getComponentInstanceFromChildren(Object componentKey) {
        String componentKeyPath = componentKey.toString();
        int ix = componentKeyPath.indexOf('/');
        if (ix != -1) {
            String firstElement = componentKeyPath.substring(0, ix);
            String remainder = componentKeyPath.substring(ix + 1, componentKeyPath.length());
            Object o = getNamedContainers().get(firstElement);
            if (o != null) {
                MutablePicoContainer child = (MutablePicoContainer) o;
                return child.getComponentInstance(remainder);
            }
        }
        return null;
    }

    protected abstract Object getComponentInstanceFromDelegate(Object componentKey);

    public final List getComponentKeys() {
        ArrayList keys = new ArrayList();
        Collection cas = getComponentAdapters();
        for (Iterator iterator = cas.iterator(); iterator.hasNext();) {
            ComponentAdapter ca = (ComponentAdapter) iterator.next();
            Object componentKey = ca.getComponentKey();
            if (componentKey instanceof Class) {
                keys.add("*" + ((Class) componentKey).getName());
            } else {
                keys.add(componentKey.toString());
            }
        }
        Iterator it = getNamedContainers().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String name = (String) entry.getKey();
            PicoContainer pc = (PicoContainer) entry.getValue();
            if (pc instanceof SoftCompositionPicoContainer) {
                List list = ((SoftCompositionPicoContainer) pc).getComponentKeys();
                for (int i = 0; i < list.size(); i++) {
                    String key = (String) list.get(i);
                    keys.add(name + "/" + key);
                }
            }
        }
        return Collections.unmodifiableList(keys);
    }

    protected abstract Map getNamedContainers();
}