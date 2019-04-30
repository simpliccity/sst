/*
 *    Copyright 2012 Information Control Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.simpliccity.sst.bean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BeanNameGeneratorExceptionTest
{
	
	private static String MESSAGE = "Bean Name Generator Exception";
	private static String CAUSE_MESSAGE = "Bean Naming Cause";
	
	/**
	 * Tests constructor which accepts only a message.
	 */
	@Test
	public void testMessageConstructor() 
	{
		BeanNameGeneratorException ex = new BeanNameGeneratorException(MESSAGE);
		assertEquals("Constructed with message only has message", MESSAGE, ex.getMessage());
		assertNull("Constructed with message only has no cause", ex.getCause());
	}
	
	/**
	 * Tests constructor which accepts both a message and a cause.
	 */
	@Test
	public void testMessageCauseConstructor() 
	{
		Throwable t = new Throwable(CAUSE_MESSAGE);
		BeanNameGeneratorException ex = new BeanNameGeneratorException(MESSAGE, t);
		assertEquals("Constructed with cause and message has cause", t, ex.getCause());
		assertTrue("Message contains specified message", ex.getMessage().indexOf(MESSAGE, 0) >= 0);
		assertTrue("Message contains cause message", ex.getMessage().indexOf(CAUSE_MESSAGE, 0) >= 0);
	}
	
}
