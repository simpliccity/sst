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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validator;
import javax.validation.groups.Default;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.binding.message.DefaultMessageContext;
import org.springframework.binding.message.Message;
import org.springframework.binding.message.MessageContext;
import org.springframework.binding.message.MessageResolver;
import org.springframework.binding.validation.ValidationContext;

@RunWith(MockitoJUnitRunner.class)
public class JavaBeanValidatorAdapterTest 
{
	private static final String[] CONSTRAINT_PATHS = {"name"};
	private static final String[] CONSTRAINT_MESSAGES = {"JavaBeanValidatorAdapterTest test violation"};
	
	private JavaBeanValidatorAdapter adapter;
	@Mock private Validator mockValidator;
	@Mock private ValidationContext mockContext;
	private MessageContext mockMessage;
	@Mock private TestObject mockModel;
	
	@Before
	public void initialize()
	{
		adapter = new JavaBeanValidatorAdapter();
		adapter.setValidator(mockValidator);
		
		mockMessage = spy(new DefaultMessageContext());
		when(mockContext.getMessageContext()).thenReturn(mockMessage);
	}
	
	/**
	 * Tests getter/setter for validator property.
	 */
	@Test
	public void testSetValidator()
	{
		assertEquals("Validator set", mockValidator, adapter.getValidator());
	}
	
	/**
	 * Tests that successful validation (no simulated constraint violations) does not generate
	 * any messages.
	 */
	@Test
	public void testPerformSuccessfulDefaultValidation()
	{
		adapter.performValidation(mockModel, mockContext, Default.class);
		
		verify(mockValidator).validate(mockModel, Default.class);
		verify(mockMessage, never()).addMessage(any(MessageResolver.class));
	}
	
	/**
	 * Tests that the correct messages are generated for simulated constraint violations.
	 */
	@Test
	public void testPerformFailedDefaultValidation()
	{
		initializeFailedConstraints();
		
		adapter.performValidation(mockModel, mockContext, Default.class);
		
		assertEquals("Expected number of messages", CONSTRAINT_MESSAGES.length, mockMessage.getAllMessages().length);
		for (Message message : mockMessage.getAllMessages())
		{
			assertTrue("Expected message", validMessage(message));
		}
	}
	
	private void initializeFailedConstraints()
	{
		Set<ConstraintViolation<TestObject>> constraints = new HashSet<ConstraintViolation<TestObject>>();
		when(mockValidator.validate(mockModel, Default.class)).thenReturn(constraints);
		
		for (int i = 0; i < CONSTRAINT_MESSAGES.length; i++)
		{
			ConstraintViolation<TestObject> constraint = mock(TestConstraintViolation.class);
			
			Collection<Path.Node> pathCollection = new ArrayList<Path.Node>();
			Path.Node node1 = mock(Path.Node.class);
			when(node1.getName()).thenReturn(CONSTRAINT_PATHS[i]);
			pathCollection.add(node1);
			Path path = mock(Path.class);
			when(path.iterator()).thenReturn(pathCollection.iterator());
			
			when(constraint.getPropertyPath()).thenReturn(path);
			when(constraint.getMessage()).thenReturn(CONSTRAINT_MESSAGES[i]);
			
			constraints.add(constraint);
		}
		
	}
	
	private boolean validMessage(Message message)
	{
		boolean result = false;
		
		for (int i = 0; i < CONSTRAINT_PATHS.length; i++)
		{
			if (CONSTRAINT_PATHS[i].equals(message.getSource()))
			{
				result = result || CONSTRAINT_MESSAGES[i].equals(message.getText());
			}
		}
		
		return result;
	}
	
}
