/*****************************************************************************
 * Copyright (C) NanoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the license.html file.                                                    *
 *                                                                           *
 * Original code by Aslak Hellesoy and Paul Hammant                          *
 *****************************************************************************/

package nanocontainer;

public class NanoTextRegistrationException extends NanoRegistrationException
{
    private final String message;

    public NanoTextRegistrationException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
