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

package org.simpliccity.sst.transform;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.simpliccity.sst.property.ValueHolder;
import org.simpliccity.styleguide.annotation.CodeStyle;
import org.simpliccity.styleguide.annotation.CodeStyles;
import org.simpliccity.styleguide.annotation.StyleCitation;
import org.simpliccity.styleguide.schema.CitationPlacement;
import org.simpliccity.styleguide.schema.CitationReferenceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

// --- StyleGuide documentation.styleguide ---
@CodeStyles
({
	@CodeStyle
	(
		categoryId="integration", 
		topicId="initialize", 
		citations={@StyleCitation(ref="springRunner.html", type=CitationReferenceType.HTML, placement=CitationPlacement.TOP)}
	),
	@CodeStyle
	(
		categoryId="documentation", 
		topicId="styleguide", 
		citations={@StyleCitation(ref="multiplestyles.html", type=CitationReferenceType.HTML, placement=CitationPlacement.TOP)}
	)
})
// --- StyleGuide documentation.styleguide ---
// --- StyleGuide integration.initialize ---
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/TransformerIT.xml" })
public class TransformerIT 
{
	// --- StyleGuide integration.initialize ---

	// --- UserGuide transform ---
	@Autowired
	private Transformer transformer;
	// --- UserGuide transform ---
	
	@Test
	public void testOutTransformation()
	{
		ValueHolder input = new ValueHolder();
		input.setValue("ABC");
		
		TransformationResult output = transformer.transform(input, TransformationResult.class);
		
		Assert.assertNotNull(output);
		Assert.assertEquals(input.getValue(), output.getResult());
	}

	@Test
	public void testInTransformation()
	{
		TransformationResult input = new TransformationResult();
		input.setResult("def");
		
		ValueHolder output = transformer.transform(input, ValueHolder.class);
		
		Assert.assertNotNull(output);
		Assert.assertEquals(input.getResult(), output.getValue());
	}
}
