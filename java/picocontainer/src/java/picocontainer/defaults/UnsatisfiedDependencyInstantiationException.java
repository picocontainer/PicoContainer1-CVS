/*****************************************************************************
 * Copyright (C) PicoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Idea by Rachel Davies, Original code by Aslak Hellesoy and Paul Hammant   *
 *****************************************************************************/

package picocontainer.defaults;

import picocontainer.PicoInstantiationException;

public class UnsatisfiedDependencyInstantiationException extends PicoInstantiationException
{
    private Class classThatNeedsDeps;

    public UnsatisfiedDependencyInstantiationException(Class classThatNeedsDeps)
    {
        this.classThatNeedsDeps = classThatNeedsDeps;
    }

    public String getMessage()
    {
        return "Class " + classThatNeedsDeps.getName() + " needs unnamed dependencies";
    }

    public Class getClassThatNeedsDeps()
    {
        return classThatNeedsDeps;
    }
}