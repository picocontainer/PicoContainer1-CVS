/*****************************************************************************
 * Copyright (C) NanoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 *****************************************************************************/
package org.nanocontainer;

import org.nanocontainer.xml.XmlFrontEnd;
import org.nanocontainer.xml.DefaultXmlFrontEnd;
import org.nanocontainer.xml.EmptyXmlConfigurationException;
import org.picocontainer.PicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoConfigurationException;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.IOException;

public class BespokeXmlFrontEnd implements XmlFrontEnd {

    private DefaultXmlFrontEnd xmlFrontEnd;
    public static boolean used;

    public BespokeXmlFrontEnd() {
        xmlFrontEnd = new DefaultXmlFrontEnd();
    }

    public PicoContainer createPicoContainer(Element rootElement, MutablePicoContainer mutablePicoContainer) throws IOException, SAXException, ClassNotFoundException, PicoConfigurationException {
        used = true;
        return xmlFrontEnd.createPicoContainer(rootElement, mutablePicoContainer);
    }

    public PicoContainer createPicoContainer(Element rootElement) throws IOException, SAXException, ClassNotFoundException, PicoConfigurationException {
        used = true;
        return xmlFrontEnd.createPicoContainer(rootElement);
    }
}