/*****************************************************************************
 * Copyright (C) PicoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Idea by Rachel Davies, Original code by Aslak Hellesoy and Paul Hammant   *
 * Ported to .NET by Jeremey Stell-Smith                                     *
 *****************************************************************************/


namespace PicoContainer
{
	public class NullStartableLifecycleManager : IStartableLifecycleManager 
	{
		public void StartComponent(object component) 
		{
		}

		public void StopComponent(object component) 
		{
		}

		public void DisposeOfComponent(object component) 
		{
		}
	}
}