/*****************************************************************************
 * Copyright (C) NanoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by Michael Ward                                    		 *
 *****************************************************************************/

package org.nanocontainer.jmx;

import javax.management.DynamicMBean;
import javax.management.MBeanInfo;

/**
 * This factory is responsible for creating instances of DynamicMBean without being dependent on one particular
 * implementation or external dependency.  
 *
 * @author Michael Ward
 * @version $Revision$
 */
public interface DynamicMBeanFactory {

	DynamicMBean create(Object componentInstance, MBeanInfo mBeanInfo);
}