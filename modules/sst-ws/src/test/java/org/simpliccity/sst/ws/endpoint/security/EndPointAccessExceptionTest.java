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

package org.simpliccity.sst.ws.endpoint.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.simpliccity.sst.service.security.ServiceAccessException;

/**
 * This is a JUnit for the methods in {@link ServiceAccessException}.
 * @author Srikaant Subramanian 
 * 
 */
public class EndPointAccessExceptionTest
{
	
	private static String message = "Endpoint Access Exception";
	private static String cause = "Invalid Access";
	
	@Test
	public void test_EndpointAccessException_String() 
	{
		ServiceAccessException eae = new ServiceAccessException(message);
		assertEquals("Constructed with message only has message", message, eae.getMessage());
		assertNull("Constructed with message only has no cause", eae.getCause());
	}
	
	@Test
	public void test_EndpointAccessException_Throwable() 
	{
		Throwable t = new Throwable(cause);
		ServiceAccessException eae = new ServiceAccessException(t);
		assertSame("Constructed with cause only has cause", t, eae.getCause());
	}
	
	
	@Test
	public void test_EndpointAccessException_String_Throwable() 
	{
		Throwable t = new Throwable(cause);
		ServiceAccessException eae = new ServiceAccessException(message, t);
		assertSame("Constructed with cause and message has cause", t, eae.getCause());
		assertEquals("Constructed with cause and message has message", message, eae.getMessage());
	}
	
}
