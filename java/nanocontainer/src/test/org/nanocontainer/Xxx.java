/*****************************************************************************
 * Copyright (C) NanoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 *****************************************************************************/
package org.nanocontainer;

import org.picocontainer.lifecycle.Lifecycle;
import junit.framework.Assert;

public abstract class Xxx implements Lifecycle {

    public static String componentRecorder = "";

    public void start() {
        componentRecorder += "<" + code();
    }

    public void stop() {
        componentRecorder += code() + ">";
    }

    public void dispose() {
        componentRecorder += "!" + code();
    }

    private String code() {
        String name = getClass().getName();
        return name.substring(name.length() - 1);
    }

    public static class A extends Xxx {}
    public static class B extends Xxx {
        public B(A a) {
            Assert.assertNotNull(a);
        }
    }
    public static class C extends Xxx {}
}