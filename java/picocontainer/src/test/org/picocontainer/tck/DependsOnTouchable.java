/*****************************************************************************
 * Copyright (C) PicoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Idea by Rachel Davies, Original code by Aslak Hellesoy and Paul Hammant   *
 *****************************************************************************/

package org.picocontainer.tck;

import junit.framework.Assert;

import java.io.Externalizable;
import java.io.ObjectInput;
import java.io.IOException;
import java.io.ObjectOutput;

/**
 * @author steve.freeman@m3p.co.uk
 */
public class DependsOnTouchable implements Externalizable {
    public Touchable touchable;

    // to satify Externalizable
    public DependsOnTouchable() {
    }

    public DependsOnTouchable(Touchable touchable) {
        Assert.assertNotNull("Touchable cannot be passed in as null", touchable);
        touchable.wasTouched();
        this.touchable = touchable;
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        // whatever
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        // whatever
    }

    public Object getTouchable() {
        return touchable;
    }


}