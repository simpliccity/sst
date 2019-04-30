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

package org.simpliccity.sst.service.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.simpliccity.sst.property.ValueHolder;
import org.simpliccity.sst.service.security.annotation.SemanticConstraint;
import org.simpliccity.sst.transform.ComplexTransformationInput;

@RunWith(MockitoJUnitRunner.class)
public class SpelSemanticContentExtractorTest 
{
	public static final String OBJECT_NAME = "object-name";
	
	public static final String OBJECT_VALUE = "object-value";
	
	public static final String PATH_SIMPLE = "name";
	
	public static final String PATH_COMPLEX = "value.value";
	
	public static final String PATH_UNKNOWN = "unknown";
	
	@Mock private ServiceSemanticContext<?> context;

	@Mock private ServiceSemanticContext<?> failureContext;

	@Mock private SemanticConstraint simpleConstraint;

	@Mock private SemanticConstraint complexConstraint;

	@Mock private SemanticConstraint unknownConstraint;

	private SemanticContentExtractor extractor = new SpelSemanticContentExtractor();

	@Before
	public void init()
	{
		ValueHolder value = new ValueHolder();
		value.setValue(OBJECT_VALUE);
		
		ComplexTransformationInput inputObject = new ComplexTransformationInput();
		inputObject.setName(OBJECT_NAME);
		inputObject.setValue(value);
		
		when(context.getPayload()).thenReturn(inputObject);
		when(failureContext.getPayload()).thenReturn(null);
		
		when(simpleConstraint.contentSpec()).thenReturn(PATH_SIMPLE);
		when(complexConstraint.contentSpec()).thenReturn(PATH_COMPLEX);
		when(unknownConstraint.contentSpec()).thenReturn(PATH_UNKNOWN);
	}

	@Test
	public void testSingleSimpleResult()
	{
		Object[] results = extractor.extractContent(context, simpleConstraint);
		
		assertEquals("Single simple path result return set size.", 1, results.length);
		
		assertEquals("Single simple path result value.", OBJECT_NAME, (String) results[0]);
	}

	@Test
	public void testSingleComplexResult()
	{
		Object[] results = extractor.extractContent(context, complexConstraint);
		
		assertEquals("Single complex path result return set size.", 1, results.length);
		
		assertEquals("Single complex path result value.", OBJECT_VALUE, (String) results[0]);
	}

	@Test
	public void testPathNotFound()
	{
		Object[] results = extractor.extractContent(context, unknownConstraint);
		
		assertNull("Path not found null result", results);
	}

	@Test
	public void testNoPayload()
	{
		Object[] results = extractor.extractContent(failureContext, simpleConstraint);
		
		assertNull("No payload null result", results);
	}
}
