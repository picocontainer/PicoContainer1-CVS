/*****************************************************************************
 * Copyright (C) MicroContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by Mike Ward                                                *
 *****************************************************************************/

package org.microcontainer.impl;

import java.io.File;
import java.net.URLClassLoader;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * @author Mike Ward
 */
public class ClassLoaderFactory {
	public static final String PROMOTED_PATH = "/MCA-INF/promoted/";
	public static final String COMPONENT_PATH = "/MCA-INF/components/";
	public static final String LIB_PATH = "/MCA-INF/lib/";
	protected McaDeployer mcaDeployer = null; // todo fix this should be configurable

	public ClassLoaderFactory() {
		mcaDeployer = new McaDeployer();
	}

	public ClassLoader build(String contextName) {
        HiddenPromotedClassLoader hiddenPromotedClassLoader = new HiddenPromotedClassLoader(getURLs(contextName, PROMOTED_PATH), this.getClass().getClassLoader());
		ClassLoader promotedClassLoader = new PromotedClassLoader(hiddenPromotedClassLoader, this.getClass().getClassLoader());
		return new StandardMicroClassLoader(getStandardApiURLs(contextName), promotedClassLoader);
	}

	protected URL[] getStandardApiURLs(String context) {
		URL[] componentURLs = getURLs(context, COMPONENT_PATH);
		URL[] promotedURLs = getURLs(context, LIB_PATH);
		int total = componentURLs.length + promotedURLs.length;
		URL[] urls = new URL[total];

		// build for promoted
		for(int i = 0; i < promotedURLs.length; i++) {
			urls[i] = promotedURLs[i];
		}

		// build for component
		for(int i = 0; i < componentURLs.length; i++) {
			urls[i + promotedURLs.length] = componentURLs[i];
		}

		return urls;
	}

	protected URL[] getURLs(String context, String path) {
		File dir = new File(mcaDeployer.getWorkingDir(), context + path);
		File[] files = dir.listFiles();

		if(files == null) {
			return new URL[0];
		}

		URL[] urls = new URL[files.length];

		try {
			for(int i = 0; i < files.length; i++) {
				urls[i] = files[i].toURL();
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return urls;
	}

}


