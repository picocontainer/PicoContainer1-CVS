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

using NUnit.Framework;

namespace PicoContainer.Test.TestModel 
{
	public class FlintstonesImpl 
	{
		public FlintstonesImpl(Wilma wilma, FredImpl fred) 
		{
			Assertion.AssertNotNull("Wilma cannot be passed in as null", wilma);
			Assertion.AssertNotNull("FredImpl cannot be passed in as null", fred);
		}
	}
}