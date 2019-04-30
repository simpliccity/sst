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
import org.simpliccity.sst.service.security.annotation.SemanticConstraint;

@RunWith(MockitoJUnitRunner.class)
public class RequestParameterSemanticContentExtractorTest 
{
	public static final String PARAM_EXISTING = "found";
	
	public static final String PARAM_MULTIPLE = "multiple";
	
	public static final String PARAM_MISSING = "missing";
	
	public static final String PARAM_VALUE = "123";
	
	@Mock private ServiceSemanticContext<?> context;
	
	@Mock private SemanticConstraint successConstraint;
	
	@Mock private SemanticConstraint multipleConstraint;

	@Mock private SemanticConstraint failureConstraint;

	private SemanticContentExtractor extractor = new RequestParameterSemanticContentExtractor();

	@Before
	public void init()
	{
		when(successConstraint.contentSpec()).thenReturn(PARAM_EXISTING);
		when(multipleConstraint.contentSpec()).thenReturn(PARAM_MULTIPLE);
		when(failureConstraint.contentSpec()).thenReturn(PARAM_MISSING);
		
		when(context.getParameter(PARAM_EXISTING)).thenReturn(new Object[] {PARAM_VALUE});
		when(context.getParameter(PARAM_MULTIPLE)).thenReturn(new Object[] {PARAM_VALUE, PARAM_EXISTING});
		when(context.getParameter(PARAM_MISSING)).thenReturn(null);		
	}
	
	@Test
	public void testSingleResult()
	{
		Object[] results = extractor.extractContent(context, successConstraint);
		
		assertEquals("Single result return set size.", 1, results.length);
		
		assertEquals("Single result value.", PARAM_VALUE, (String) results[0]);
	}

	@Test
	public void testMultipleResults()
	{
		Object[] results = extractor.extractContent(context, multipleConstraint);
		
		assertEquals("Multiple results return set size.", 2, results.length);
		
		assertEquals("Multiple results first value.", PARAM_VALUE, (String) results[0]);
		assertEquals("Multiple results second value.", PARAM_EXISTING, (String) results[1]);
	}

	@Test
	public void testParameterNotFound()
	{
		Object[] results = extractor.extractContent(context, failureConstraint);
		
		assertNull("Parameter not found null result", results);
	}
}
