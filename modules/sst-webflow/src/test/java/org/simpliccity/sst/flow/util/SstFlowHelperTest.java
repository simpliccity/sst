/*
 *    Copyright 2013 Information Control Corporation
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

package org.simpliccity.sst.flow.util;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.binding.message.MessageContext;
import org.springframework.binding.message.MessageResolver;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;
import org.springframework.webflow.test.MockExternalContext;

@RunWith(MockitoJUnitRunner.class)
public class SstFlowHelperTest 
{
	private SstFlowHelper sstFlowHelper = new SstFlowHelper();
	private MockExternalContext externalContext = new MockExternalContext();
	@Mock private MessageContext messageContext;
	@Mock private RequestContext requestContext;
	
	@Before
	public void initialize()
	{
		// Setup behavior of mock RequestContext		
		when(requestContext.getExternalContext()).thenReturn(externalContext);
		when(requestContext.getMessageContext()).thenReturn(messageContext);
		
		// Configure RequestContextHolder to return mock RequestContext
		RequestContextHolder.setRequestContext(requestContext);
	}
	
	@Test(expected=SstFlowException.class)
	public void testNoContext()
	{
		RequestContextHolder.setRequestContext(null);
	
		sstFlowHelper.addSessionAttribute("Test", "Value");		
	}
	
	@Test
	public void testGenerateNull()
	{
		Assert.assertNull("Generates null value", sstFlowHelper.generateNull());
	}
	
	@Test
	public void testAddSessionAttribute()
	{
		
		sstFlowHelper.addSessionAttribute("Test", "Value");
		
		String value = (String) externalContext.getSessionMap().get("Test");
		
		Assert.assertEquals("Session value added", value, "Value");
	}
	
	@Test
	public void testAddInfoMessage()
	{
		sstFlowHelper.addInfoMessage("TestMessage", this, "parm1", "parm2");
		
		verify(messageContext, times(1)).addMessage(any(MessageResolver.class));
	}

	@Test
	public void testAddWarnMessage()
	{
		sstFlowHelper.addWarnMessage("TestMessage", this, "parm1", "parm2");
		
		verify(messageContext, times(1)).addMessage(any(MessageResolver.class));
	}

	@Test
	public void testAddErrorMessage()
	{
		sstFlowHelper.addErrorMessage("TestMessage", this);
		
		verify(messageContext, times(1)).addMessage(any(MessageResolver.class));
	}
}
