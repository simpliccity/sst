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

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.simpliccity.styleguide.annotation.CodeStyle;
import org.simpliccity.styleguide.annotation.StyleCitation;
import org.simpliccity.styleguide.schema.CitationPlacement;
import org.simpliccity.styleguide.schema.CitationReferenceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactoryBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@CodeStyle
(
	categoryId="integration", 
	topicId="dirtyContext", 
	marker="--- StyleGuide integration.dirtyContext ---; highlight -sghighlight-",
	citations={@StyleCitation(ref="dirtiesAnnotationPerMethod.html", type=CitationReferenceType.HTML, placement=CitationPlacement.TOP)}
)
// --- StyleGuide integration.dirtyContext ---
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/JdbcPropertyConfigurationIT.xml" })
// -sghighlight-
@DirtiesContext(classMode=ClassMode.AFTER_EACH_TEST_METHOD)
// -sghighlight-
public class JdbcPropertyConfigurationIT 
{
// --- StyleGuide integration.dirtyContext ---
	
	 @Autowired
	 @Value("#{propertiesFactory.testProperty}")
	 private String testProperty;
	 
	 @Autowired
	 private ValueHolder valueHolder;
	 
	 @Autowired
	 private EmbeddedDatabaseFactoryBean dbFactory;
	 
	 @After
	 public void cleanup()
	 {
		 dbFactory.destroy();
	 }
	 
	 @Test
	 public void testPropertyResolution()
	 {
		 assertEquals("Property resolution", "abcdefg", testProperty);
	 }
	 
	 @Test
	 public void testPlaceholderResolution()
	 {
		 assertEquals("Placeholder resolution", "http://www.simpliccity.org", valueHolder.getValue());
	 }	 
}
