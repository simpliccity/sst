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

package org.simpliccity.sst.transform.annotation;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class TransformationBeanMetadataLookupContainerTest 
{
	@Test
	public void testDiscriminator()
	{
		TransformationBeanMetadataLookupContainer container = new TransformationBeanMetadataLookupContainer();

		Assert.assertSame("Valid discriminator", Transformation.class, container.getLookupDiscriminator());
	}
	
	@Test
	public void testAccessors()
	{
		TransformationBeanMetadataLookupContainer container = new TransformationBeanMetadataLookupContainer();
		
		container.setSource(String.class.getCanonicalName());
		container.setTarget(this.getClass().getCanonicalName());
		
		Assert.assertSame("Valid source", String.class.getCanonicalName(), container.getSource());
		Assert.assertSame("Valid target", this.getClass().getCanonicalName(), container.getTarget());
	}
	
	@Test
	public void testAttributes()
	{
		TransformationBeanMetadataLookupContainer container = new TransformationBeanMetadataLookupContainer();
		
		container.setSource(String.class.getCanonicalName());
		container.setTarget(this.getClass().getCanonicalName());

		Map<String, Object> attributes = container.getMetadataAttributes();
		
		Assert.assertSame("Valid source attribute", String.class.getCanonicalName(), attributes.get(TransformationBeanMetadataLookupContainer.KEY_ATTRIBUTE_SOURCE));
		Assert.assertSame("Valid target attribute", this.getClass().getCanonicalName(), attributes.get(TransformationBeanMetadataLookupContainer.KEY_ATTRIBUTE_TARGET));
	}
}
