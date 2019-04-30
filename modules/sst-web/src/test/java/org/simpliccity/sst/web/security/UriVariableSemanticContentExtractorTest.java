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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.simpliccity.sst.service.security.SemanticContentExtractor;
import org.simpliccity.sst.service.security.ServiceSemanticContext;
import org.simpliccity.sst.service.security.annotation.SemanticConstraint;

@RunWith(MockitoJUnitRunner.class)
public class UriVariableSemanticContentExtractorTest 
{
	public static final String PATTERN_EXISTING = "/test/content/{parameter}";
	
	public static final String PATTERN_MULTIPLE = "/test/{path}/{parameter}";
	
	public static final String PATTERN_MISSING = "/junk/{path}";
	
	public static final String COMPONENT_PATH = "content";
	
	public static final String COMPONENT_PARAMETER = "value";
	
	public static final String URI = "/test/" + COMPONENT_PATH + "/" + COMPONENT_PARAMETER;
	
	@Mock private ServiceSemanticContext<?> context;
	
	@Mock private SemanticConstraint successConstraint;
	
	@Mock private SemanticConstraint multipleConstraint;

	@Mock private SemanticConstraint failureConstraint;

	private SemanticContentExtractor extractor = new UriVariableSemanticContentExtractor();

	@Before
	public void init()
	{
		when(successConstraint.contentSpec()).thenReturn(PATTERN_EXISTING);
		when(multipleConstraint.contentSpec()).thenReturn(PATTERN_MULTIPLE);
		when(failureConstraint.contentSpec()).thenReturn(PATTERN_MISSING);
		
		when(context.getAddress()).thenReturn(URI);
	}
	
	@Test
	public void testSingleResult()
	{
		Object[] results = extractor.extractContent(context, successConstraint);
		
		assertEquals("Single result return set size.", 1, results.length);
		
		assertEquals("Single result value.", COMPONENT_PARAMETER, (String) results[0]);
	}

	@Test
	public void testMultipleResults()
	{
		Object[] results = extractor.extractContent(context, multipleConstraint);
		
		assertEquals("Multiple results return set size.", 2, results.length);
		
		assertEquals("Multiple results first value.", COMPONENT_PATH, (String) results[0]);
		assertEquals("Multiple results second value.", COMPONENT_PARAMETER, (String) results[1]);
	}

	@Test
	public void testVariableNotFound()
	{
		Object[] results = extractor.extractContent(context, failureConstraint);
		
		assertNull("No variables found null result", results);
	}
}
