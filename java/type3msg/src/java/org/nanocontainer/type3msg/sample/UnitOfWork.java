/*****************************************************************************
 * Copyright (C) NanoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by                                                          *
 *****************************************************************************/

package org.nanocontainer.type3msg.sample;

/**
 * Created by IntelliJ IDEA.
 * User: skizz
 * Date: Sep 10, 2003
 * Time: 10:13:18 PM
 * To change this template use Options | File Templates.
 */
public class UnitOfWork {
    String name;

    public UnitOfWork(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
