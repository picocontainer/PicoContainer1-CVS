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



using System;
using System.Reflection;

namespace PicoContainer
{
	public class DefaultComponentFactory : IComponentFactory 
	{
		public virtual object CreateComponent(Type compType, ConstructorInfo constructor, object[] args)
		{
			return Activator.CreateInstance(compType, args);
		}
	}
}