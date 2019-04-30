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

package org.simpliccity.sst.transform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.simpliccity.sst.transform.annotation.TransformationType;

@RunWith(MockitoJUnitRunner.class)
public class TransformationDescriptorTest 
{
	private static final String NAME_DESCRIPTOR = "TEST_DESCRIPTOR";
	
	@Mock private TransformationProxy proxy; 
	
	@Test
	public void testGetName()
	{
		TransformationDescriptor descriptor = new TransformationDescriptor(NAME_DESCRIPTOR, proxy, TransformationType.NONE);
		
		assertEquals("Name value", NAME_DESCRIPTOR, descriptor.getName());
	}
	
	@Test
	public void testGetProxy()
	{
		TransformationDescriptor descriptor = new TransformationDescriptor(NAME_DESCRIPTOR, proxy, TransformationType.NONE);

		assertEquals("Proxy value", proxy, descriptor.getProxy());
	}
	
	@Test
	public void testGetDirection()
	{
		TransformationDescriptor descriptor = new TransformationDescriptor(NAME_DESCRIPTOR, proxy, TransformationType.NONE);

		assertEquals("Direction value", TransformationType.NONE, descriptor.getDirection());
	}
	
	@Test
	public void testNoTransform()
	{
		TransformationDescriptor descriptor = new TransformationDescriptor(NAME_DESCRIPTOR, proxy, TransformationType.NONE);
		
		TransformationResult result = descriptor.performTransformation(new Object(), TransformationResult.class, false);
		
		assertNull("Transform direction NONE", result);		
	}
	
	@Test
	public void testOutboundTransform() throws UnsupportedTransformationException
	{
		when(proxy.performOutTransformation(any(), eq(TransformationResult.class), anyBoolean())).thenReturn(new TransformationResult("Success"));
		
		TransformationDescriptor descriptor = new TransformationDescriptor(NAME_DESCRIPTOR, proxy, TransformationType.OUT);
		
		Object input = new Object();
		
		TransformationResult result = descriptor.performTransformation(input, TransformationResult.class, false);
		
		assertNotNull("Outbound transformation result", result);
		
		verify(proxy, times(1)).performOutTransformation(input, TransformationResult.class, false);
			
	}

	@Test
	public void testInboundTransform() throws UnsupportedTransformationException
	{
		when(proxy.performInTransformation(any(), eq(TransformationResult.class), anyBoolean())).thenReturn(new TransformationResult("Success"));
		
		TransformationDescriptor descriptor = new TransformationDescriptor(NAME_DESCRIPTOR, proxy, TransformationType.IN);
		
		Object input = new Object();
		
		TransformationResult result = descriptor.performTransformation(input, TransformationResult.class, false);
		
		assertNotNull("Inbound transformation result", result);
		
		verify(proxy, times(1)).performInTransformation(input, TransformationResult.class, false);
			
	}
	
	@Test
	public void testFailedTransform() throws UnsupportedTransformationException
	{
		doThrow(new UnsupportedTransformationException("Expected failure")).when(proxy).performInTransformation(any(), eq(TransformationResult.class), anyBoolean());
		
		TransformationDescriptor descriptor = new TransformationDescriptor(NAME_DESCRIPTOR, proxy, TransformationType.IN);
		
		Object input = new Object();
		
		TransformationResult result = descriptor.performTransformation(input, TransformationResult.class, false);
		
		assertNull("Failed transformation result", result);
		
		verify(proxy, times(1)).performInTransformation(input, TransformationResult.class, false);
			
	}	
}
