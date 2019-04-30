/*
 *    Copyright 2011 Information Control Corporation
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

package org.simpliccity.sst.property;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.simpliccity.styleguide.annotation.CodeStyle;
import org.simpliccity.styleguide.annotation.StyleCitation;
import org.simpliccity.styleguide.schema.CitationPlacement;
import org.simpliccity.styleguide.schema.CitationReferenceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactoryBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@CodeStyle
(
	categoryId="integration", 
	topicId="embeddedDb",
	citations=
		{
			@StyleCitation(ref="PropertyLoaderTest.xml", type=CitationReferenceType.XML, placement=CitationPlacement.TOP, marker="embeddedDb"),
			@StyleCitation(ref="sql/PropertiesTest.sql", type=CitationReferenceType.TEXT, placement=CitationPlacement.TOP)
		}
)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/PropertyLoaderTest.xml" })
@DirtiesContext(classMode=ClassMode.AFTER_EACH_TEST_METHOD)
public class OrderedDelegatingPropertyLoaderTest 
{
	@Autowired
	@Qualifier("Sequence")
	private PropertyLoader sequenceLoader;

	@Autowired
	@Qualifier("AlternateSequence")
	private PropertyLoader alternateSequenceLoader;

	// --- StyleGuide integration.embeddedDb ---
	@Autowired
	private EmbeddedDatabaseFactoryBean dbFactory;
	
	@After
	public void cleanup()
	{
		dbFactory.destroy();
	}
	// --- StyleGuide integration.embeddedDb ---
	
	@Test
	public void loadSequenceProperties() throws Exception
	{
		Properties props = new Properties();
		sequenceLoader.loadProperties(props);
		
		assertEquals("Standard sequence - test property", "HIJKLMN", props.getProperty("testProperty"));
		
		assertEquals("Standard sequence - test URL", "http://www.simpliccity.org", props.getProperty("testURL"));
		
		assertEquals("Standard sequence - new property", "TEST", props.getProperty("newProperty"));
	}
	
	@Test
	public void loadAlternateSequenceProperties() throws Exception
	{
		Properties props = new Properties();
		alternateSequenceLoader.loadProperties(props);
		
		assertEquals("Alternate sequence - test property", "abcdefg", props.getProperty("testProperty"));
		
		assertEquals("Alternate sequence - test URL", "http://www.simpliccity.org", props.getProperty("testURL"));
		
		assertEquals("Alternate sequence - new property", "TEST", props.getProperty("newProperty"));
	}
}
