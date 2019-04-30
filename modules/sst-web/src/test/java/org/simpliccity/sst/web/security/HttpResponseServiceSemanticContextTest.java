/*
 *    Copyright 2016 Information Control Company
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

package org.simpliccity.sst.web.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class HttpResponseServiceSemanticContextTest 
{
	public static final String PARAM_EXISTING = "found";
	
	public static final String PARAM_UNKNOWN = "unknown";
	
	public static final String PARAM_VALUE = "true";
	
	private MockHttpServletRequest request = new MockHttpServletRequest("POST", "/index");
	
	private MockHttpServletResponse response = new MockHttpServletResponse();
	
	private HttpResponseServiceSemanticContext context = new HttpResponseServiceSemanticContext(request, response);

	@Before
	public void init()
	{
		request.setParameter(PARAM_EXISTING, PARAM_VALUE);
	}
	
	@Test
	public void testGetContext()
	{
		assertEquals("Get context.", request, context.getContext());
	}

	@Test
	public void testGetRequest()
	{
		assertEquals("Get request.", request, context.getRequest());
	}
	
	@Test
	public void testGetPayload() throws IOException
	{
		assertEquals("Get payload.", response.getOutputStream(), context.getPayload());
	}
	
	@Test
	public void testGetAddress()
	{
		assertEquals("Get address.", request.getRequestURI(), context.getAddress());
	}
	
	@Test
	public void testGetExistingParameter()
	{
		Object[] values = context.getParameter(PARAM_EXISTING);
		
		assertEquals("Get existing parameter size.", 1, values.length);
		assertEquals("Get existing parameter.", PARAM_VALUE, values[0]);
	}

	@Test
	public void testGetUnknownParameter()
	{
		assertNull("Get unknown parameter.", context.getParameter(PARAM_UNKNOWN));
	}
}
