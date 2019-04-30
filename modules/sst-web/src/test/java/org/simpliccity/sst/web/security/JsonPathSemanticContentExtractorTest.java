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
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.simpliccity.sst.service.security.SemanticContentExtractor;
import org.simpliccity.sst.service.security.ServiceSemanticContext;
import org.simpliccity.sst.service.security.annotation.SemanticConstraint;

@RunWith(MockitoJUnitRunner.class)
public class JsonPathSemanticContentExtractorTest 
{
	@Mock private ServiceSemanticContext<?> context;
	
	@Mock private SemanticConstraint constraint;
	
	private SemanticContentExtractor extractor = new JsonPathSemanticContentExtractor();
	
	@Test
	public void testSingleResult()
	{
		InputStream stream = new ByteArrayInputStream("{\"field\":\"value\"}".getBytes());
		when(context.getPayload()).thenReturn(stream);
		
		when(constraint.contentSpec()).thenReturn("field");
		
		Object[] results = extractor.extractContent(context, constraint);
		
		assertEquals("Single result return set size.", 1, results.length);
		
		assertEquals("Single result value.", "value", (String) results[0]);
	}
	
	@Test
	public void testMultipleResults()
	{
		InputStream stream = new ByteArrayInputStream("{\"parent\":[{\"field\":\"value\"}, {\"field\":\"newValue\"}]}".getBytes());
		when(context.getPayload()).thenReturn(stream);
		
		when(constraint.contentSpec()).thenReturn("parent[*].field");
		
		Object[] results = extractor.extractContent(context, constraint);
		
		assertEquals("Multiple result return set size.", 2, results.length);
		
		assertEquals("Multiple result first value.", "value", (String) results[0]);
		assertEquals("Multiple result second value.", "newValue", (String) results[1]);		
	}
	
	@Test
	public void testNotJSON()
	{
		InputStream stream = new ByteArrayInputStream("junk".getBytes());
		when(context.getPayload()).thenReturn(stream);
		
		when(constraint.contentSpec()).thenReturn("field");
		
		Object[] results = extractor.extractContent(context, constraint);
		
		assertNull("Not JSON null result.", results);
	}
	
	@Test
	public void testPathNotFound()
	{
		InputStream stream = new ByteArrayInputStream("{\"field\":\"value\"}".getBytes());
		when(context.getPayload()).thenReturn(stream);
		
		when(constraint.contentSpec()).thenReturn("missing");
		
		Object[] results = extractor.extractContent(context, constraint);

		assertNull("Path not found null result.", results);
	}
	
	@Test
	public void testNoPayload()
	{
		when(context.getPayload()).thenReturn(null);
		
		when(constraint.contentSpec()).thenReturn("field");
		
		Object[] results = extractor.extractContent(context, constraint);
		
		assertNull("Missing payload null result.", results);		
	}
}
