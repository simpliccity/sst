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

package org.simpliccity.sst.flow.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.binding.message.DefaultMessageContext;
import org.springframework.binding.message.Message;
import org.springframework.binding.message.MessageContext;
import org.springframework.binding.validation.ValidationContext;

@RunWith(MockitoJUnitRunner.class)
public class JavaBeanValidatorAdapterIT 
{
	private JavaBeanValidatorAdapter adapter;
	@Mock private ValidationContext mockContext;
	private MessageContext mockMessage;
	
	@Before
	public void initialize()
	{
		adapter = new JavaBeanValidatorAdapter();
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		adapter.setValidator(validator);
		
		mockMessage = spy(new DefaultMessageContext());
		when(mockContext.getMessageContext()).thenReturn(mockMessage);
	}
	
	/**
	 * Tests that a successful validation does not generate any messages.
	 */
	@Test
	public void testPerformSuccessfulDefaultValidation()
	{
		TestObject model = new TestObject();
		model.setName("Test");
		
		model.setChild(model.new TestChild());
		model.getChild().setValue("123");
		
		adapter.performValidation(model, mockContext, Default.class);
		
		assertEquals("No messages expected", 0, mockMessage.getAllMessages().length);
	}
	
	/**
	 * Tests that failed validations generate messages for the correct sources.
	 */
	@Test
	public void testPerformFailedDefaultValidation()
	{
		TestObject model = new TestObject();
		model.setChild(model.new TestChild());
		
		adapter.performValidation(model, mockContext, Default.class);
		
		String[] allowedSources = new String[] {"name", "child.value"};
		for (Message message : mockMessage.getAllMessages())
		{
			assertTrue("Expected message from violation", validMessage(message, allowedSources));
		}
	}
	
	/**
	 * Tests that failed validations generate messages for the correct sources using prefixed names.
	 */
	@Test
	public void testPrefixPerformFailedDefaultValidation()
	{
		TestObject model = new TestObject();
		model.setChild(model.new TestChild());
		
		adapter.performValidation("Test", model, mockContext, Default.class);
		
		String[] allowedSources = new String[] {"Test.name", "Test.child.value"};
		for (Message message : mockMessage.getAllMessages())
		{
			assertTrue("Expected message from violation", validMessage(message, allowedSources));
		}
	}
	
	private boolean validMessage(Message message, String[] allowedSources)
	{
		boolean result = false;
		
		for (String source : allowedSources)
		{
			result = result || source.equals(message.getSource());
		}
		
		return result;
	}
	
}
