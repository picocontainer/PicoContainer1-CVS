/*****************************************************************************
 * Copyright (C) PicoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the license.html file.                                                    *
 *                                                                           *
 * Original code by Aslak Hellesoy and Paul Hammant                          *
 *****************************************************************************/

package picocontainer;

public class DuplicateComponentRegistrationException extends PicoRegistrationException
{
    private Class clazz;

    public DuplicateComponentRegistrationException(Class clazz)
    {
        this.clazz = clazz;
    }

    public Class getDuplicateClass()
    {
        return clazz;
    }

    public String getMessage()
    {
        return "Class " + clazz.getName() + " duplicated";
    }
}
