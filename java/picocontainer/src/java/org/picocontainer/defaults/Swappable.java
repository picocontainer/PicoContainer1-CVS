package org.picocontainer.defaults;

/**
 * Interface implemented by all proxy instances created by
 * {@link ImplementationHidingComponentAdapter).
 *
 * @author Aslak Helles&oslash;y
 * @version $Revision$
 */
public interface Swappable {
    /**
     * Swaps the subject behind the proxy with a new instance.
     * (The underscores in the method name are to reduce the risk of method name clashes
     * with other interfaces).
     * @param newSubject the new subject the proxy will delegate to.
     * @return the old subject
     */
    Object __hotSwap(Object newSubject);
}