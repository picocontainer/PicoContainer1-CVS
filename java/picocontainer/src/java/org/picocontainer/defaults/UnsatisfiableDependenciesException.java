/*****************************************************************************
 * Copyright (C) PicoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by                                                          *
 *****************************************************************************/
package org.picocontainer.defaults;

import org.picocontainer.PicoIntrospectionException;
import org.picocontainer.ComponentAdapter;

import java.util.Set;

/**
 * @author Aslak Helles&oslash;y
 * @version $Revision$
 */
public class UnsatisfiableDependenciesException extends PicoIntrospectionException {

    private final ComponentAdapter instantiatingComponentAdapter;
    private final Set failedDependencies;

    public UnsatisfiableDependenciesException(ComponentAdapter instantiatingComponentAdapter, Set failedDependencies) {
        super(instantiatingComponentAdapter.getComponentImplementation().getName() + " doesn't have any satisfiable constructors. Unsatisfiable dependencies: " + failedDependencies);
        this.instantiatingComponentAdapter = instantiatingComponentAdapter;
        this.failedDependencies = failedDependencies;
    }

    public ComponentAdapter getUnsatisfiableComponentAdapter() {
        return instantiatingComponentAdapter;
    }

    public Set getUnsatisfiableDependencies() {
        return failedDependencies;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UnsatisfiableDependenciesException)) return false;

        final UnsatisfiableDependenciesException noSatisfiableConstructorsException = (UnsatisfiableDependenciesException) o;

        if (!instantiatingComponentAdapter.equals(noSatisfiableConstructorsException.instantiatingComponentAdapter)) return false;
        if (!failedDependencies.equals(noSatisfiableConstructorsException.failedDependencies)) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = instantiatingComponentAdapter.hashCode();
        result = 29 * result + failedDependencies.hashCode();
        return result;
    }
}
