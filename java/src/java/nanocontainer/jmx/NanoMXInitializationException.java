/*****************************************************************************
 * Copyright (Cc) NanoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by James Strachan and Mauro Talevi                          *
 *****************************************************************************/
package nanocontainer.jmx;

import picocontainer.PicoInitializationException;

/**
 * A registration exception caused trying to register the component with JMX
 * 
 * @author James Strachan
 * @author Mauro Talevi
 * @version $Revision$
 */
public class NanoMXInitializationException extends PicoInitializationException {

    private Throwable cause;
    
    public NanoMXInitializationException(Throwable cause) {
        this.cause = cause;
    }

    /**
     * @return the underlying exception
     */
    public Throwable getCause() {
        return cause;
    }
    
    public String getLocalizedMessage() {
        return cause.getLocalizedMessage();
    }

    public String getMessage() {
        return cause.getMessage();
    }

}
