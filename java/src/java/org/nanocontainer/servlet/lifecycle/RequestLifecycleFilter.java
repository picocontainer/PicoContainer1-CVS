/*****************************************************************************
 * Copyright (C) NanoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by Joe Walnes                                               *
 *****************************************************************************/


package org.nanocontainer.servlet.lifecycle;


import org.nanocontainer.servlet.holder.SessionScopeObjectHolder;

import org.nanocontainer.servlet.holder.RequestScopeObjectHolder;

import org.nanocontainer.servlet.lifecycle.BaseLifecycleListener;

import org.nanocontainer.servlet.ObjectHolder;

import org.nanocontainer.servlet.ObjectInstantiator;


import javax.servlet.*;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpSession;

import java.io.IOException;


import org.picocontainer.PicoContainer;


public class RequestLifecycleFilter extends BaseLifecycleListener implements Filter {


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        HttpSession session = httpRequest.getSession(true);

        ServletContext context = session.getServletContext();



        // grab the parent container

        ObjectHolder parentHolder = new SessionScopeObjectHolder(session, CONTAINER_KEY);

        PicoContainer parentContainer = (PicoContainer) parentHolder.get();



        // build a container

        PicoContainer container = getFactory(context).buildContainerWithParent(parentContainer, "request");



        // and a means to instantiate new objects in the container

        ObjectInstantiator instantiater = getFactory(context).buildInstantiator(container);



        // hold on to them

        ObjectHolder containerHolder = new RequestScopeObjectHolder(httpRequest, CONTAINER_KEY);

        containerHolder.put(container);


        ObjectHolder instantiaterHolder = new RequestScopeObjectHolder(httpRequest, INSTANTIATER_KEY);

        instantiaterHolder.put(instantiater);


        try {


            // process the incoming request

            filterChain.doFilter(request, response);


        } finally {


            // shutdown container

            destroyContainer(context, containerHolder);


        }

    }


    public void init(FilterConfig filterConfig) throws ServletException {

    }


    public void destroy() {

    }

}
