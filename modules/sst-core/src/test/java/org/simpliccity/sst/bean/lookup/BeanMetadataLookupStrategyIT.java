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

package org.simpliccity.sst.bean.lookup;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.simpliccity.sst.transform.ComplexTransformationInput;
import org.simpliccity.sst.transform.ComplexTransformationResult;
import org.simpliccity.sst.transform.annotation.TransformationBeanMetadataLookupContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/BeanMetadataLookupStrategyIT.xml" })
public class BeanMetadataLookupStrategyIT 
{
	@Autowired
	private BeanMetadataLookupManager metadataLookup;

	@Test
	public void testAnnotatedLookup()
	{
		TransformationBeanMetadataLookupContainer metadata = new TransformationBeanMetadataLookupContainer();
		metadata.setSource(ComplexTransformationInput.class.getCanonicalName());
		metadata.setTarget(ComplexTransformationResult.class.getCanonicalName());
		
		Object bean = metadataLookup.lookupBeanByMetadata(metadata);
		
		Assert.assertNull("Noop annotated bean lookup", bean);
	}
	
	@Test
	public void testTypedLookup()
	{
		TestableComponentMetadataLookupContainer metadata = new TestableComponentMetadataLookupContainer();
		
		metadata.setResultType("Success");
		TestableComponent successBean = (TestableComponent) metadataLookup.lookupBeanByMetadata(metadata);
		Assert.assertNull("Noop typed bean lookup", successBean);
	}
}
