/*****************************************************************************
 * Copyright (C) PicoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Idea by Rachel Davies, Original code by Aslak Hellesoy and Paul Hammant   *
 *****************************************************************************/

package picocontainer;

public class NullContainer implements Container {
    public boolean hasComponent(Class compType) {
        return false;
    }

    public Object getComponent(Class compType) {
        return null;
    }

    public Object[] getComponents() {
        return new Object[0];
    }
}
