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

package org.simpliccity.sst.bean.lookup.config.annotation;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.simpliccity.sst.bean.lookup.BeanMetadataLookupManager;
import org.simpliccity.sst.bean.lookup.FailingTestableComponent;
import org.simpliccity.sst.bean.lookup.PassingTestableComponent;
import org.simpliccity.sst.bean.lookup.TestAnnotatedBeanMetadataLookupContainer;
import org.simpliccity.sst.bean.lookup.TestableComponent;
import org.simpliccity.sst.bean.lookup.TestableComponentMetadataLookupContainer;
import org.simpliccity.sst.transform.ComplexTestTransformer;
import org.simpliccity.sst.transform.ComplexTransformationInput;
import org.simpliccity.sst.transform.ComplexTransformationResult;
import org.simpliccity.sst.transform.annotation.TransformationBeanMetadataLookupContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=BeanMetadataLookupManagerTestConfig.class)
@ActiveProfiles("beanMetadata")
public class BeanMetadataLookupManagerAnnotationIT 
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
		
		Assert.assertTrue("Annotated bean lookup", bean instanceof ComplexTestTransformer);
	}
	
	@Test
	public void testUnsupportedAnnotatedLookup()
	{
		TestAnnotatedBeanMetadataLookupContainer metadata = new TestAnnotatedBeanMetadataLookupContainer();
		metadata.setValue("Test");
		
		Object bean = metadataLookup.lookupBeanByMetadata(metadata);
		
		Assert.assertNull("Unsupported annotated bean lookup", bean);		
	}
	
	@Test
	public void testTypedLookup()
	{
		TestableComponentMetadataLookupContainer metadata = new TestableComponentMetadataLookupContainer();
		
		metadata.setResultType("Success");
		TestableComponent successBean = (TestableComponent) metadataLookup.lookupBeanByMetadata(metadata);
		Assert.assertSame("Typed bean lookup - SUCCESS", PassingTestableComponent.RESULT_PASS, successBean.getTestResult());
		
		metadata.setResultType("Failure");
		TestableComponent failureBean = (TestableComponent) metadataLookup.lookupBeanByMetadata(metadata);
		Assert.assertSame("Typed bean lookup - FAILURE", FailingTestableComponent.RESULT_FAIL, failureBean.getTestResult());
		
		metadata.setResultType("Curved");
		TestableComponent curvedBean = (TestableComponent) metadataLookup.lookupBeanByMetadata(metadata);
		Assert.assertNull("Typed bean lookup - CURVED", curvedBean);
	}
}
