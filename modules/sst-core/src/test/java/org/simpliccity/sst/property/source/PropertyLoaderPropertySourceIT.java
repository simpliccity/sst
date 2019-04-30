/*
 *    Copyright 2014 Information Control Corporation
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

package org.simpliccity.sst.property.source;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.simpliccity.sst.property.ValueHolder;
import org.simpliccity.styleguide.annotation.CodeStyle;
import org.simpliccity.styleguide.annotation.StyleCitation;
import org.simpliccity.styleguide.schema.CitationPlacement;
import org.simpliccity.styleguide.schema.CitationReferenceType;
import org.springframework.context.support.GenericXmlApplicationContext;

@CodeStyle
(
	categoryId="integration", 
	topicId="embeddedDb",
	citations={@StyleCitation(ref="org.simpliccity.sst.property.source.JdbcPropertyLoaderBuilder", type=CitationReferenceType.JAVA, placement=CitationPlacement.BOTTOM, displayRef=true)}
)
public class PropertyLoaderPropertySourceIT 
{
	// --- StyleGuide integration.embeddedDb ---
	private JdbcPropertyLoaderBuilder builder;
	private GenericXmlApplicationContext context;
	
	@Before
	public void initialize()
	{
		// --- UserGuide propertyLoaderPropertySource ---
		builder = new JdbcPropertyLoaderBuilder();
		
		// Configure application context
		context = new GenericXmlApplicationContext();
		// -ughighlight-
		context.getEnvironment().getPropertySources().addLast(new PropertyLoaderPropertySource("testSource", builder.getPropertyLoader()));
		// -ughighlight-
		context.load("PropertySourceIT.xml");
		context.refresh();
		// --- UserGuide propertyLoaderPropertySource ---
	}
	
	@After
	public void cleanup()
	{
		builder.cleanup();
	}
	// --- StyleGuide integration.embeddedDb ---
	
	 @Test
	 public void testPropertyResolution()
	 {
		 assertEquals("Correct value for testProperty property", "abcdefg", context.getEnvironment().getProperty("testProperty"));
	 }
	 
	 @Test
	 public void testPlaceholderResolution()
	 {
		 ValueHolder valueHolder = context.getBean(ValueHolder.class);
		 
		 System.out.println(valueHolder.getValue());
		 assertEquals("Correct value for testURL property placeholder", "http://www.simpliccity.org", valueHolder.getValue());
	 }	 
}
